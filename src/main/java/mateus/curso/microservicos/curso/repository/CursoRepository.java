package mateus.curso.microservicos.curso.repository;

import mateus.curso.microservicos.curso.model.Curso;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends PagingAndSortingRepository<Curso,Long> {
}
