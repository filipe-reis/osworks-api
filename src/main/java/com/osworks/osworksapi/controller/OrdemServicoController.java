package com.osworks.osworksapi.controller;

import com.osworks.osworksapi.domain.model.OrdemServico;
import com.osworks.osworksapi.domain.repository.OrdemServicoRepository;
import com.osworks.osworksapi.domain.service.GestaoOrdemServicoService;
import com.osworks.osworksapi.represetantion.model.OrdemServicoInputModel;
import com.osworks.osworksapi.represetantion.model.OrdemServicoModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ordens-servico")
public class OrdemServicoController {

    @Autowired
    private GestaoOrdemServicoService gestaoOrdemServico;

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdemServicoModel criar(@Valid @RequestBody OrdemServicoInputModel ordemServicoInputModel){
        OrdemServico ordemServico = toEntity(ordemServicoInputModel);
        return toModel(gestaoOrdemServico.criar(ordemServico));
    }

    @GetMapping
    public List<OrdemServicoModel> listar(){
        return toCollectionModel(ordemServicoRepository.findAll());
    }

    @GetMapping("/{ordemServicoId}")
    public ResponseEntity<OrdemServicoModel> buscar(@PathVariable Long ordemServicoId){
        Optional<OrdemServico> ordemServico = ordemServicoRepository.findById(ordemServicoId);

        if(ordemServico.isPresent()){
            OrdemServicoModel ordemServicoModel = toModel(ordemServico.get());
            return ResponseEntity.ok(ordemServicoModel);
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{ordemServicoId}/finalizacao")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void finalizar(@PathVariable Long ordemServicoId){
        gestaoOrdemServico.finalizar(ordemServicoId);
    }



    /**
     * Faz o parse do Domain Model para o Representation model
     * @param ordemServico - Domain Model
     * @return ordemServicoModel - Representation Model
     */
    private OrdemServicoModel toModel(OrdemServico ordemServico){
        return modelMapper.map(ordemServico, OrdemServicoModel.class);
    }

    /**
     * Faz o parse de uma lista de Domain Model para uma lista de Representation Model
     * @param ordemServicos - Lista Domain Model
     * @return ordemServicosModel - Lista de Representation Model
     */
    private List<OrdemServicoModel> toCollectionModel(List<OrdemServico> ordemServicos){
        return ordemServicos.stream()
                .map(ordemServico -> toModel(ordemServico))
                .collect(Collectors.toList());
    }

    /**
     * Faz o parse de um Representation Model para um Domain Model
     * @param ordemServicoInputModel - entrada de dados na api na forma representation model
     * @return ordemServico - Domain Model
     */
    private OrdemServico toEntity(OrdemServicoInputModel ordemServicoInputModel){
        return modelMapper.map(ordemServicoInputModel, OrdemServico.class);
    }
}
