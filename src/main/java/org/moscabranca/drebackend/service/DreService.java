package org.moscabranca.drebackend.service;

import org.moscabranca.drebackend.model.Dre;
import org.moscabranca.drebackend.model.Receita;
import org.moscabranca.drebackend.model.Despesa;
import org.moscabranca.drebackend.repository.DreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DreService {

    @Autowired
    private DreRepository dreRepository;

    public Dre salvarDre(Dre dre) {
        calcularDre(dre);  // Executa o cálculo antes de salvar
        return dreRepository.save(dre);
    }

    public List<Dre> listarTodos() {
        return dreRepository.findAll();
    }

    public Dre buscarPorId(Long id) {
        return dreRepository.findById(id).orElse(null);
    }

    public void deletarDre(Long id) {
        dreRepository.deleteById(id);
    }

    /**
     * Calcula os valores da DRE (Demonstração do Resultado do Exercício)
     * @param dre DRE a ser calculada
     */
    void calcularDre(Dre dre) {
        BigDecimal receitaBrutaTotal = calcularReceitaBrutaTotal(dre.getReceitas());
        BigDecimal despesasOperacionais = calcularDespesasOperacionais(dre.getDespesas());
        BigDecimal cmv = dre.getCmv() != null ? dre.getCmv() : calcularCmv(dre.getDespesas());

        BigDecimal comissoes = calcularComissoes(dre.getReceitas());

        dre.setReceitaBrutaTotal(receitaBrutaTotal);
        dre.setCmv(cmv);
        dre.setComissoes(comissoes);

        // Receita líquida = Receita Bruta - CMV - Comissões
        dre.setReceitaLiquida(receitaBrutaTotal.subtract(cmv).subtract(comissoes));
        dre.setDespesasOperacionais(despesasOperacionais);

        // EBITDA = Receita Líquida - Despesas Operacionais
        dre.setEbitda(dre.getReceitaLiquida().subtract(despesasOperacionais));

        // Define taxa de imposto padrão se não estiver especificada
        BigDecimal taxaImposto = dre.getTaxaImposto() != null ? dre.getTaxaImposto() : BigDecimal.valueOf(0.08);
        dre.setTaxaImposto(taxaImposto);

        // Calcula os impostos com base no EBITDA e na taxa de imposto
        dre.setImpostos(calcularImpostos(dre.getEbitda(), taxaImposto));

        // Lucro Líquido = EBITDA - Depreciação - Impostos
        BigDecimal depreciacao = dre.getDepreciacao() != null ? dre.getDepreciacao() : BigDecimal.ZERO;
        dre.setLucroLiquido(dre.getEbitda().subtract(depreciacao).subtract(dre.getImpostos()));
    }

    /**
     * Calcula a Receita Bruta Total a partir das receitas.
     * @param receitas Lista de Receitas
     * @return Receita Bruta Total
     */
    private BigDecimal calcularReceitaBrutaTotal(List<Receita> receitas) {
        return receitas.stream()
                .map(receita -> receita.getReceitaBrutaTotal() != null ? receita.getReceitaBrutaTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula as Despesas Operacionais excluindo CMV e comissões.
     * @param despesas Lista de Despesas
     * @return Despesas Operacionais
     */
    private BigDecimal calcularDespesasOperacionais(List<Despesa> despesas) {
        return despesas.stream()
                .filter(despesa -> despesa.getCmv() == null && despesa.getComissoes() == null)
                .map(despesa -> despesa.getValor() != null ? despesa.getValor() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula o Custo das Mercadorias Vendidas (CMV) somando apenas as despesas classificadas como CMV.
     * @param despesas Lista de Despesas
     * @return Custo das Mercadorias Vendidas
     */
    private BigDecimal calcularCmv(List<Despesa> despesas) {
        return despesas.stream()
                .map(despesa -> despesa.getCmv() != null ? despesa.getCmv() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula as Comissões com base nas receitas classificadas como comissões.
     * @param receitas Lista de Receitas
     * @return Comissões
     */
    private BigDecimal calcularComissoes(List<Receita> receitas) {
        return receitas.stream()
                .map(receita -> receita.getComissoes() != null ? receita.getComissoes() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula os Impostos aplicando a taxa de imposto sobre o EBITDA.
     * @param ebitda EBITDA
     * @param taxaImposto Taxa de Imposto
     * @return Impostos
     */
    private BigDecimal calcularImpostos(BigDecimal ebitda, BigDecimal taxaImposto) {
        return ebitda.multiply(taxaImposto);
    }
}