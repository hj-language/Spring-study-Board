package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/writeForm")
    public String boardWriteForm() {
        return "boardwrite";
    }

    @GetMapping("/board")
    public String getBoardList(Model model,
                               @PageableDefault(page=0, size=10, sort="id", direction= Sort.Direction.DESC) Pageable pageable,
                               String searchKeyword) {

        Page<Board> list;

        if (searchKeyword == null) {
            list = boardService.list(pageable);
        } else {
            list = boardService.search(searchKeyword, pageable);
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "boardlist";
    }

    @GetMapping("/board/{id}")
    public String getBoard(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("board", boardService.get(id));
        return "boardview";
    }

    @GetMapping("/board/{id}/patch")
    public String boardModifyForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("board", boardService.get(id));
        return "boardmodify";
    }

    @PostMapping("/board")
    public String postBoard(Board board, MultipartFile file, Model model) throws IOException {
        boardService.write(board, file);
        model.addAttribute("message", "Write Success");
        model.addAttribute("searchUrl", "/board");
        return "message";
    }

    @PostMapping("/board/{id}/patch")
    public String patchBoard(@PathVariable("id") Integer id, Board board, MultipartFile file, Model model) throws IOException {
        boardService.update(id, board, file);
        model.addAttribute("message", "Patch Success");
        model.addAttribute("searchUrl", "/board");
        return "message";
    }

    @GetMapping("/board/{id}/delete")
    public String deleteBoard(@PathVariable("id") Integer id) {
        boardService.delete(id);
        return "redirect:/board";
    }
}
