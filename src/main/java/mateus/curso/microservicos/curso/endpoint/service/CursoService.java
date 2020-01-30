package mateus.curso.microservicos.curso.endpoint.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mateus.curso.microservicos.curso.model.Curso;
import mateus.curso.microservicos.curso.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CursoService {
    private final CursoRepository cursoRepository;

    public Iterable<Curso> list (Pageable pageable) {
        log.info("Listando todos os cursos");
        return cursoRepository.findAll(pageable);
    }
}
