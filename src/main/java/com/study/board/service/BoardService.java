package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    public void write(Board board, MultipartFile file) throws IOException {
        String dirPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File saveFile = new File(dirPath, fileName);
        file.transferTo(saveFile);

        board.setFilename(fileName);
        board.setFilepath("/files/"+fileName);
        boardRepository.save(board);
    }

    public Board get(Integer id) {
        return boardRepository.findById(id).get();
    }

    public Page<Board> list(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    public Page<Board> search(String searchKeyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    public void update(Integer id, Board newBoard, MultipartFile file) throws IOException {
        Board originBoard = this.get(id);
        originBoard.setTitle(newBoard.getTitle());
        originBoard.setContent(newBoard.getContent());
        this.write(originBoard, file);
    }
    public void delete(Integer id) {
        boardRepository.deleteById(id);
    }
}
