package mateus.curso.microservicos.core.repository;

import mateus.curso.microservicos.core.model.Curso;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends PagingAndSortingRepository<Curso,Long> {
}
