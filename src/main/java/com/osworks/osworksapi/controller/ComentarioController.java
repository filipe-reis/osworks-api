package com.osworks.osworksapi.controller;

import com.osworks.osworksapi.domain.exception.EntidadeNaoEncontradaException;
import com.osworks.osworksapi.domain.model.Comentario;
import com.osworks.osworksapi.domain.model.OrdemServico;
import com.osworks.osworksapi.domain.repository.OrdemServicoRepository;
import com.osworks.osworksapi.domain.service.GestaoOrdemServicoService;
import com.osworks.osworksapi.represetantion.model.ComentarioInput;
import com.osworks.osworksapi.represetantion.model.ComentarioModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ordens-servico/{ordemServicoId}/comentarios")
public class ComentarioController {

    @Autowired
    private GestaoOrdemServicoService gestaoOrdemServico;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @GetMapping
    private List<ComentarioModel> listar(@PathVariable  Long ordemServicoId){
        OrdemServico ordemServico = ordemServicoRepository.findById(ordemServicoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de Serviço não encontrada"));

        return toCollectionModel(ordemServico.getComentarios());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ComentarioModel adicionar(@PathVariable Long ordemServicoId,
                                     @Valid @RequestBody ComentarioInput comentarioInput){

        Comentario comentario = gestaoOrdemServico.adicionarComentario(ordemServicoId, comentarioInput.getDescricao());

        return toModel(comentario);
    }


    private ComentarioModel toModel(Comentario comentario){
        return modelMapper.map(comentario, ComentarioModel.class);
    }

    private List<ComentarioModel> toCollectionModel(List<Comentario> comentarios){
        return comentarios.stream()
                .map(comentario -> toModel(comentario))
                .collect(Collectors.toList());
    }
}
