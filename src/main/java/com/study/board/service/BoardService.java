package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    //글 작성 처리
    public void write(Board board, MultipartFile file) throws Exception{

        //저장할 경로
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

        //파일에 이름을 붙일 랜덤 이름을 생성
        UUID uuid = UUID.randomUUID();

        //랜던 파일이름_원래파일이름
        if(file != null) {
            String fileName = uuid + "_" + file.getOriginalFilename();

            File saveFile = new File(projectPath, fileName);

            file.transferTo(saveFile);

            board.setFilename(fileName);
            board.setFilepath("/Files/" + fileName);
        }


        boardRepository.save(board);

    }

    //게시글 리스트 목록 보여주기 처리
    public Page<Board> boardList(Pageable pageable) {

        return  boardRepository.findAll(pageable);
    }

    //특정 개시글 처리
    public Board boardView(Integer id) {

        return boardRepository.findById(id).get();
    }

    // 검색창에서 검색한거 처리
    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    //특정 게시글 삭제 void는 return type이 없다.
    public void boardDelete(Integer id) {
        boardRepository.deleteById(id);
    }
}
