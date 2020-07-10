package com.dictation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.dictation.vo.LectureVO;

@Repository
@Mapper
public interface LectureMapper {	//lectureMapper.xml에서 이름,명령어 지정	
	
	//insert
	public void insert(LectureVO lecture);

	//according to id delete
	public void delete(String lecture_no);	

	//according to user Of id modify
	public void update(LectureVO lecture);

	//강좌번호 생성할때 중복되는 강좌가 있는지 검사하는코드
	public int lecture_no_search(int lecture_no);
	
	//according to id query
	public LectureVO getById(int lecture_no);

	//All queries
	public List<LectureVO> list();

	
}
