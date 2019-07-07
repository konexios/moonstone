package com.arrow.pegasus.dashboard.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arrow.pegasus.dashboard.data.Board;
import com.arrow.pegasus.repo.RepositoryExtension;

public interface BoardRepositoryExtension extends RepositoryExtension<Board> {

	public Page<Board> findBoards(Pageable pageable, BoardSearchParams params);

	public List<Board> findBoards(BoardSearchParams params);
}
