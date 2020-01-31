package mateus.curso.microservicos.curso.endpoint.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mateus.curso.microservicos.core.model.Curso;
import mateus.curso.microservicos.curso.endpoint.service.CursoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/admin/curso")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CursoController {
    private final CursoService cursoService;

    @GetMapping(produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Curso>> list (Pageable pageable) {
        return new ResponseEntity<>(cursoService.list(pageable), HttpStatus.OK);
    }
}
