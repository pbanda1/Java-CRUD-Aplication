package hr.algebra.humanitarnaorganizacija.interface_;

import hr.algebra.humanitarnaorganizacija.exception.RepoException;

import java.util.List;
import java.util.Optional;

public interface ICrud  <E, Id>{

    List<E>findAll();  /* vrati mi entitete kao listu*/

    Optional<E> findById(Id id) ; /*vrati opcionalni entitet kojeg mozda nema */

    void save (E entity) throws RepoException;

    void deleteById(Id id) throws RepoException;

    void update(E entity) throws RepoException;

    default int count ()  {return findAll().size(); }
}
