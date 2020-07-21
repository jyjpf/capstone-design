package com.dictation.controller;

import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import com.dictation.service.LectureService;
import com.dictation.vo.LectureVO;
import com.dictation.vo.UserVO;


@CrossOrigin("*")
@RestController
@RequestMapping(value="/api/lecture")
public class LectureController {
	
	@Autowired
	private LectureService lectureService;
	
	//���� �����ϱ�
	@CrossOrigin("*")
	@PostMapping(produces = "application/json;charset=UTF-8")
	public void insert(@RequestBody LectureVO lecture, HttpServletRequest request) throws Exception {
		//����Ʈ���� lecture_nm, grade, ban�޾ƿ�
		//�鿣�� lecture_no, school_cd �߰�(enroll_st_dt�� ���Ŀ� ���ۿ��� �߰�)
		
		HttpSession session = request.getSession();
		UserVO user_session=(UserVO)session.getAttribute("user");
		
		lecture.setTeacher_id(user_session.getUser_id());
		
		int lecture_no=rand(7); //DB�� ���� lecture_noĮ���� ����(7�ڸ� ��������)
		Object db_lec_no = lectureService.lecture_no_search(lecture_no); //�����ѳ����� ��� �̹��ִ��� �˻�(������ null, ������ lecture_no��)
		
		//lecture_no�ߺ����� �ʴ� ������ ����
		while(db_lec_no!=null) {
			lecture_no=rand(7);
			db_lec_no = lectureService.lecture_no_search(lecture_no);
		
		}
		//System.out.println("������� ���°�");
		lecture.setLecture_no(lecture_no);
		System.out.println("��"+lecture_no);
		System.out.println("��" + lecture.getBan());
		lectureService.insert(lecture);
				
	}
	
	
	public int rand(int num) {//num�ڸ���ȣ ��������
		Random random = new Random();
		String numStr="";//num�ڸ���ȣ ������
		int numInt=0;//num�ڸ���ȣ ������
		
		for(int i=0; i<num; i++) {
			String ran=Integer.toString(random.nextInt(10));
			numStr += ran;
		}
		numInt=Integer.parseInt(numStr);
		return numInt;
	}


      //according to id delete
	@GetMapping(value="/delete/{lecture_no}")
	public void delete(@PathVariable("lecture_no") String lecture_no) {
		lectureService.delete(lecture_no);
	}
	//modify
	//lecture_no�� ���ƾ� ��
	@PostMapping(value="/update")
	public void update(@RequestBody LectureVO lecture) {
		lectureService.update(lecture);
	}

	//lecture_no���� int������ �ٲܰ�
	//according to id Query students
	@GetMapping(value="/get/{lecture_no}")
	public LectureVO getById(@PathVariable("lecture_no") int lecture_no) {
		LectureVO lecture = lectureService.getById(lecture_no);
		return lecture;
	}
	
	//All queries
	@RequestMapping(value="/list")
	public List<LectureVO> list(){
		return lectureService.list();
	}
	
	//���µ���, ������û �������� lecture_no ���ǰ� ����
	//���߿��� post�� lecture_no �� �ٰ�
	@GetMapping(value = "/lecture_no/{lecture_no}")
	public String lecture_no(@PathVariable("lecture_no") int lecture_no, HttpServletRequest request) throws Exception {
		
		System.out.println("lecture_no�� ���� ���ǰ��� ��");
		
		HttpSession session = request.getSession();
		session.setAttribute("lecture_no", lecture_no);
		int lecture_session=(int)session.getAttribute("lecture_no");
		System.out.println("lecture_no ���ǰ� :" +lecture_session);

	    return "lecture_no";
	}
	
	
	//���ǰ� Ȯ���� ����� �޼ҵ�(test��)
	@GetMapping(value = "/session")
	public String session(HttpServletRequest request) throws Exception {

		HttpSession session = request.getSession();

	    System.out.println("lecture_no ���ǰ� :" +(int)session.getAttribute("lecture_no"));
	    
	    //��� ���ǰ� Ȯ��
	    Enumeration se = session.getAttributeNames();
	    while(se.hasMoreElements()){
	    	String getse = se.nextElement()+"";
	    	System.out.println("@@@@@@@ session : "+getse+" : "+session.getAttribute(getse));
	    }


	    // ���ǿ��� �����.
	    //session.invalidate();
	    //System.out.println("������ user_id ���ǰ� :" +session.getAttribute("user_id"));
	    //System.out.println("������ lecture_no ���ǰ� :" +session.getAttribute("lecture_no"));
	    return "login/user_id&lecture_no";
	}
	
	//������ ������ ������ ���¸��
	@RequestMapping(value="/teach_mylec")
	public List<LectureVO> teacher_mylec(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		UserVO user_session=(UserVO)session.getAttribute("user");
	
		System.out.println("teach_mylec���� user_id ���ǰ� : "+user_session.getUser_id());
				
		return lectureService.teacher_mylec(user_session.getUser_id());
	}
	
	//�л�ȭ�� ��ü���� ����Ʈ�� ���½�û����
	@RequestMapping(value="/student_lec_list")
	public List<LectureVO> student_lec_list(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		UserVO user_session=(UserVO)session.getAttribute("user");
	
		System.out.println("student_lec_list���� user_id ���ǰ� : "+user_session.getUser_id());
				
		return lectureService.student_lec_list(user_session.getUser_id());
	}
	
	//�л� ������ ������û�ؼ� ���ε� ���¸�� ���� ���� �ڵ�
	@RequestMapping(value="/student_mylec")
	public List<LectureVO> student_mylec(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		//UserVO user_session=(UserVO)session.getAttribute("user");
	
		//System.out.println("student_mylec���� user_id ���ǰ� : "+user_session.getUser_id());
				
		return lectureService.student_mylec("test2");
	}
		
}
