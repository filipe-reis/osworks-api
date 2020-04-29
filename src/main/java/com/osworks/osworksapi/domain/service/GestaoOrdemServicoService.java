package com.osworks.osworksapi.domain.service;

import com.osworks.osworksapi.domain.exception.EntidadeNaoEncontradaException;
import com.osworks.osworksapi.domain.exception.NegocioException;
import com.osworks.osworksapi.domain.model.Cliente;
import com.osworks.osworksapi.domain.model.Comentario;
import com.osworks.osworksapi.domain.model.OrdemServico;
import com.osworks.osworksapi.domain.model.StatusOrdemServico;
import com.osworks.osworksapi.domain.repository.ClienteRepository;
import com.osworks.osworksapi.domain.repository.ComentarioRepository;
import com.osworks.osworksapi.domain.repository.OrdemServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class GestaoOrdemServicoService {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    public OrdemServico criar(OrdemServico ordemServico){

        Cliente cliente = clienteRepository.findById(ordemServico.getCliente().getId())
                .orElseThrow(() -> new NegocioException("Cliente não encontrado!"));

        ordemServico.setCliente(cliente);
        ordemServico.setStatus(StatusOrdemServico.ABERTA);
        ordemServico.setDataAbertura(OffsetDateTime.now());

        return ordemServicoRepository.save(ordemServico);
    }

    public Comentario adicionarComentario(Long ordemServicoId, String descricao){

        OrdemServico ordemServico = buscarOrdemServico(ordemServicoId);

        Comentario comentario = new Comentario();
        comentario.setDataEnvio(OffsetDateTime.now());
        comentario.setDescricao(descricao);
        comentario.setOrdemServico(ordemServico);

        return comentarioRepository.save(comentario);
    }

    public void finalizar(Long ordemServicoId){
        OrdemServico ordemServico = buscarOrdemServico(ordemServicoId);

        ordemServico.finalizar();

        ordemServicoRepository.save(ordemServico);
    }

    private OrdemServico buscarOrdemServico(Long ordemServicoId){
        return ordemServicoRepository.findById(ordemServicoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de Serviço não encontrada"));
    }
}
