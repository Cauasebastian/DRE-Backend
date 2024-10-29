package org.moscabranca.drebackend.service;

import org.moscabranca.drebackend.model.Dre;
import org.moscabranca.drebackend.model.Receita;
import org.moscabranca.drebackend.model.Despesa;
import org.moscabranca.drebackend.model.repository.DreRepository;
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
    private void calcularDre(Dre dre) {
        BigDecimal receitaBrutaTotal = calcularReceitaBrutaTotal(dre.getReceitas());
        BigDecimal despesasOperacionais = calcularDespesasOperacionais(dre.getDespesas());
        BigDecimal cmv = calcularCmv(dre.getDespesas());
        BigDecimal comissoes = calcularComissoes(dre.getReceitas());

        dre.setReceitaBrutaTotal(receitaBrutaTotal);
        dre.setCmv(cmv);
        dre.setComissoes(comissoes);
        dre.setReceitaLiquida(receitaBrutaTotal.subtract(cmv).subtract(comissoes));
        dre.setDespesasOperacionais(despesasOperacionais);
        dre.setEbitda(dre.getReceitaLiquida().subtract(despesasOperacionais));

        // Taxa de imposto adaptável para empresas de diferentes setores
        BigDecimal taxaImposto = dre.getTaxaImposto() != null ? dre.getTaxaImposto() : BigDecimal.valueOf(0.155);
        dre.setImpostos(calcularImpostos(dre.getEbitda(), taxaImposto));

        dre.setLucroLiquido(dre.getEbitda().subtract(dre.getImpostos()));
    }

    /**
     * Calcula a Receita Bruta Total a partir das receitas.
     * @param receitas Lista de Receitas
     * @return Receita Bruta Total
     */
    private BigDecimal calcularReceitaBrutaTotal(List<Receita> receitas) {
        return receitas.stream()
                .map(Receita::getReceitaBrutaTotal)
                .filter(total -> total != null)
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
                .map(Despesa::getValor)
                .filter(valor -> valor != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula o Custo das Mercadorias Vendidas (CMV) somando apenas as despesas classificadas como CMV.
     * @param despesas Lista de Despesas
     * @return Custo das Mercadorias Vendidas
     */
    private BigDecimal calcularCmv(List<Despesa> despesas) {
        return despesas.stream()
                .map(Despesa::getCmv)
                .filter(cmv -> cmv != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula as Comissões com base nas receitas classificadas como comissões.
     * @param receitas Lista de Receitas
     * @return Comissões
     */
    private BigDecimal calcularComissoes(List<Receita> receitas) {
        return receitas.stream()
                .map(Receita::getComissoes)
                .filter(comissoes -> comissoes != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula os Impostos aplicando uma taxa fixa de 15.5% sobre o EBITDA.
     * @param ebitda EBITDA
     * @param taxaImposto Taxa de Imposto
     * @return Impostos
     */
    private BigDecimal calcularImpostos(BigDecimal ebitda, BigDecimal taxaImposto) {
        return ebitda.multiply(taxaImposto);
    }
}