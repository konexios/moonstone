package com.arrow.pegasus.dashboard.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.arrow.pegasus.dashboard.data.Board;

@Repository
public interface BoardRepository extends MongoRepository<Board, String>, BoardRepositoryExtension {

    public Board findByNameAndUserId(String name, String userId);

    public List<Board> findByUserId(String userId);

    public List<Board> findByCategoryAndUserId(String userId, String category);

    public List<Board> findByCategory(String category);

}
