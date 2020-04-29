package com.osworks.osworksapi.domain.repository;

import com.osworks.osworksapi.domain.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
}
