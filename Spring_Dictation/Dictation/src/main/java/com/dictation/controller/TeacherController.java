package com.dictation.controller;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dictation.mapper.CourseMapper;
import com.dictation.service.CourseService;
import com.dictation.service.EnrollService;
import com.dictation.service.LectureService;
import com.dictation.service.Term_cdService;
import com.dictation.service.UserService;
import com.dictation.vo.CourseVO;
import com.dictation.vo.EnrollVO;
import com.dictation.vo.LectureVO;
import com.dictation.vo.UserVO;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
 


@CrossOrigin("*")
@RestController
@RequestMapping(value="/api/teacher")
public class TeacherController {//������ ��Ʈ�ѷ�
	@Autowired
	private CourseService courseService;
	@Autowired
	private EnrollService enrollService;
	@Autowired
	private LectureService lectureService;
	@Autowired
	private UserService userService;
	
	//�ܰ��ȣ, ���׹�ȣ, ������ vue���� ������(CourseVO 1������ insert)
	//������ ȭ��-�޾ƾ��� ��Ϲ�ư
	@PostMapping(produces = "application/json;charset=UTF-8", value="/course")
	public void insert(@RequestParam Map<String, Object> map,@Param(value = "file") MultipartFile file, HttpServletRequest request) throws Exception{
		//����Ʈ���忡�� course_no, question_no, question ��������
				
		//lecture_no�� ���ǰ����� �����ͼ� ����
		HttpSession session = request.getSession();
		int lecture_session=(int)session.getAttribute("lecture_no");
		
		int course_no=Integer.parseInt((String)map.get("course_no"));
		int question_no=Integer.parseInt((String)map.get("question_no"));
		String question=(String)map.get("question");
		String originalfileName = null;
		String save_file_nm=null;
		
		CourseVO course = new CourseVO();
		course.setLecture_no(lecture_session);
		course.setCourse_no(course_no);
		course.setQuestion_no(question_no);
		course.setQuestion(question);
		
		if(file.isEmpty()){ //���ε��� ������ ���� ��
            System.out.println("���Ͼ���");
        }else {
        	System.out.println("file ���� !!");
    		
    		//���� �̸�������(FILE_NM)
    		originalfileName = file.getOriginalFilename();
    	
    		/*
    		String fileUrl=ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(originalfileName)
                    .toUriString();
            */
    		
    			
    		//SAVE_FILE_NM
    		UUID uuid =UUID.randomUUID();
    		save_file_nm=uuid.toString() +"_" +originalfileName;
    				
    		//���� ������ ��η� ����(save_file_nm �����̸����� ����)
    		File dest = new File("C:/Temp/" + save_file_nm);
    		file.transferTo(dest);
    		
    		System.out.println("�����̸� : "+originalfileName);
    		System.out.println("���ο� �����̸� : "+save_file_nm);
    		//System.out.println("���ϰ�� : "+fileUrl);
        }
		
		course.setFile_nm(originalfileName);
		course.setSave_file_nm(save_file_nm);		
		
		courseService.insert(course);
	}
	
	//������ȭ��- �޾ƾ��� ������ư
	@PostMapping(value="/course/dic_modify")
	public void dic_modify(@RequestParam Map<String, Object> map,@Param(value = "file_nm") MultipartFile file_nm, HttpServletRequest request) throws Exception{
		//����Ʈ���忡�� course_no, question_no, question, file_nm, change_file ��������
		
		//lecture_no�� ���ǰ����� �����ͼ� ����
		HttpSession session = request.getSession();
		int lecture_session=(int)session.getAttribute("lecture_no");
		
		System.out.println("111111");
		int course_no=Integer.parseInt((String)map.get("course_no"));
		int question_no=Integer.parseInt((String)map.get("question_no"));
		String question=(String)map.get("question");
		boolean change_file2=Boolean.parseBoolean((String)map.get("change_file"));
		String originalfileName = null;
		String save_file_nm=null;
		
		CourseVO course1 = new CourseVO();
		course1.setLecture_no(lecture_session);
		course1.setCourse_no(course_no);
		course1.setQuestion_no(question_no);
		course1.setQuestion(question);
		
		System.out.println("22222");
		courseService.dic_modify_question(course1);
		System.out.println("33333");
		if(change_file2==true) {//������ ����������
			if(file_nm.isEmpty()){ //���ε��� ������ ���� ��
	            System.out.println("���Ͼ���");
	        }else {
	        	System.out.println("file ���� !!");
	    		
	    		//���� �̸�������(FILE_NM)
	    		originalfileName = file_nm.getOriginalFilename();
	    			
	    		//SAVE_FILE_NM
	    		UUID uuid =UUID.randomUUID();
	    		save_file_nm=uuid.toString() +"_" +originalfileName;
	    				
	    		//���� ������ ��η� ����(save_file_nm �����̸����� ����)
	    		File dest = new File("C:/Temp/" + save_file_nm);
	    		file_nm.transferTo(dest);
	    		
	    		System.out.println("�����̸� : "+originalfileName);
	    		System.out.println("���ο� �����̸� : "+save_file_nm);
	    		//System.out.println("���ϰ�� : "+fileUrl);
	        }
			CourseVO course2 = new CourseVO();
			course2.setLecture_no(lecture_session);
			course2.setCourse_no(course_no);
			course2.setQuestion_no(question_no);
			course2.setFile_nm(originalfileName);
			course2.setSave_file_nm(save_file_nm);
			
			//�������� ����
			String delete_filenm=courseService.getById(course2).getSave_file_nm();//������ �����̸�
			File delete_file=new File("C:/Temp/"+delete_filenm);//������ ����
			delete_file.delete();//���� ����
			
			courseService.dic_modify_file(course2);
		}
	}
	
	
	//������ȭ�� ����� �޾ƾ��� �ִ�ܰ�
	@GetMapping(value="/course/max_dic_course")
	public int max_dic_course(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		int lecture_session=(int)session.getAttribute("lecture_no");
		return courseService.max_dic_course(lecture_session);
	}
	
	//������- �޾ƾ��� ���� ������
	@GetMapping(value="/course/dic_answers/{course_no}")
	public List<CourseVO> dic_answers(@PathVariable("course_no") int course_no, HttpServletRequest request) {
		CourseVO course2=new CourseVO();
		
		HttpSession session = request.getSession();
		int lecture_session=(int)session.getAttribute("lecture_no");
		course2.setLecture_no(lecture_session);
		course2.setCourse_no(course_no);
		
		System.out.println("this is dic_answer, i am finish_yn: "+ courseService.dic_answers(course2).get(1).getFinish_yn());
		
		//����
		List<CourseVO> course_sort=courseService.dic_answers(course2);
		Collections.sort(course_sort);
	
		return course_sort;
	}
	
	//�������� ���� �л��� enroll���̺� insert
	//���ǰ����� insert
	@PostMapping(produces = "application/json;charset=UTF-8", value="/enroll")
	public void insert(@RequestBody EnrollVO enroll, HttpServletRequest request) {
		
		//lecture_no
		HttpSession session = request.getSession();
		enroll.setLecture_no((int)session.getAttribute("lecture_no"));
				
		enrollService.insert(enroll);
	}
	
	//according to id delete
	//������ȭ��-�л��� ���� enroll���̺��� delete
	@PostMapping(value="/enroll/delete")
	public void delete(@RequestBody EnrollVO enroll) {
		enrollService.delete(enroll);
	}
	
	//���ǰ� lecture_no�����ͼ� update
	//������ȭ��-��û��Ȳ-���ι�ư �������� �л��� ���ν�����
	@GetMapping(value="/enroll/update/{user_id}")
	public void update_request(@PathVariable("user_id") String user_id, HttpServletRequest request) { //user_id, lecture_no�� �ʼ�

		//lecture_no
		HttpSession session = request.getSession();
		int lecture_no=(int)session.getAttribute("lecture_no");

		enrollService.update_request(lecture_no, user_id);
	}
	//��û��Ȳ ����Ʈ
	@PostMapping(value="/enroll/list_request")
	public List<UserVO> list_request(HttpServletRequest request){
		//lecture_no
		HttpSession session = request.getSession();
		int lecture_no=(int)session.getAttribute("lecture_no");
		return enrollService.list_request(lecture_no);
	}
	
	//���� �����ϱ�
	@CrossOrigin("*")
	@PostMapping(produces = "application/json;charset=UTF-8", value="/lecture")
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
	
	//������ ������ ������ ���¸��
	@RequestMapping(value="/lecture/teach_mylec")
	public List<LectureVO> teacher_mylec(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		UserVO user_session=(UserVO)session.getAttribute("user");
	
		System.out.println("teach_mylec���� user_id ���ǰ� : "+user_session.getUser_id());
				
		return lectureService.teacher_mylec(user_session.getUser_id());
	}
	
	
	//insert DB users, enroll from excel file
	//�������ε� : �������Ϸκ��� DB�� users, enroll���̺� �� insert
	@PostMapping(value="/excelup")
	public int excelup(@Param(value = "file") MultipartFile file, HttpServletRequest request) throws Exception{
		//Connection connection = null;
		int error_yn=0;//������ �� insert�ƴ��� ���� ��ȯ(��������: 0, ����:1)
		//SAVE_FILE to this path
		UUID uuid =UUID.randomUUID();
		String save_file_path="C:/Temp/�������ε�/"+uuid;//���� ���� �ӽ� ���� ���
		//user
		HttpSession session = request.getSession();
		UserVO user_session=(UserVO)session.getAttribute("user");
		
		try {    		
    		//���� ������ ��η� ����(save_file_nm �����̸����� ����)
    		File dest = new File(save_file_path);
    		file.transferTo(dest);
    		
            long start = System.currentTimeMillis();
            FileInputStream inputStream = new FileInputStream(save_file_path);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();
            Date date=new Date();
            System.out.println("excelup.1111");
            //connection = DriverManager.getConnection(jdbcURL, username, password);
            //connection.setAutoCommit(false);
  
            //String sql = "INSERT INTO students (name, enrolled, progress) VALUES (?, ?, ?)";
            //PreparedStatement statement = connection.prepareStatement(sql);    
             
            int count = 0;
             
            rowIterator.next(); // skip the header row
             
            while (rowIterator.hasNext()) {
            	UserVO user=new UserVO();
            	EnrollVO enroll=new EnrollVO();
                Row nextRow = rowIterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                String name="";//�л����̵�
                System.out.println("excelup.2222");
                
                //insert users
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    System.out.println("excelup.3333");
 
                    int columnIndex = nextCell.getColumnIndex();
 
                    switch (columnIndex) {
                    case 0:
                        name = getStringValue(nextCell);
                        user.setUser_id(name);
                        //statement.setString(1, name);
                        break;
                    case 1:
                    	String pw = getStringValue(nextCell);
                        user.setPw(pw);
                    	//int progress = (int) nextCell.getNumericCellValue();
                        //statement.setInt(3, progress);
                    case 2:
                    	String school_cd = getStringValue(nextCell);
                        user.setSchool_cd(school_cd);
                    case 3:
                    	String kor_nm = getStringValue(nextCell);
                        user.setKor_nm(kor_nm);
                    case 4:
                    	String end_nm = getStringValue(nextCell);
                        user.setEnd_nm(end_nm);
                    case 5:
                    	int grade = getIntValue(nextCell);
                        user.setGrade(grade);
                    case 6:
                    	String ban = getStringValue(nextCell);
                        user.setBan(ban);
                    case 7:
                    	String cel_phone_no = getStringValue(nextCell);
                        user.setCel_phone_no(cel_phone_no);
                    case 8:
                    	String hom_phone_no = getStringValue(nextCell);
                        user.setHom_phone_no(hom_phone_no);
                    case 9:
                    	String gender_cd = getStringValue(nextCell);
                    	if(gender_cd.equals("0")) {
                    		user.setGender_cd("002001");
                    	}else if(gender_cd.equals("1")){
                    		user.setGender_cd("002002");
                    	}
                    case 10:
                    	String email = getStringValue(nextCell);
                        user.setEmail(email);
                    
                    }
                    
 
                }
                user.setPosition_cd("003003");
                user.setInput_id(user_session.getUser_id());
                userService.insert(user);
                
                //enroll insert
        		enroll.setLecture_no((int)session.getAttribute("lecture_no"));
        		enroll.setUser_id(name);
        		enroll.setApproval_cd("����");
        		enroll.setApproval_dt(date);
        		enroll.setInput_id(user_session.getUser_id());
        		enrollService.insert(enroll);
                
                //statement.addBatch();
                 
                /*if (count % batchSize == 0) {
                    statement.executeBatch();
                } */             
 
            }
            System.out.println("excelup.4444");	
            workbook.close();
             
            // execute the remaining queries
            //statement.executeBatch();
  
            //connection.commit();
            //connection.close();
             
            long end = System.currentTimeMillis();
            System.out.printf("Import done in %d ms\n", (end - start));
            error_yn=0;
             
        } catch (Exception ex1) {
            System.out.println("Error reading file");
            ex1.printStackTrace();
            error_yn=1;
		} /*
			 * catch (SQLException ex2) { System.out.println("Database error");
			 * ex2.printStackTrace(); }
			 */
		System.out.println("excelup.6666");
		
		//���� ����(������ �ƴ� �ٸ����Ͽø��� ������ �ȵǴ� ���� ����)
		File delete_file=new File(save_file_path);//������ ����
		delete_file.delete();//���� ����
		
		return error_yn;//�������� ��ȯ(��������: 0, ����:1)
	}	
	
    /**
     * �������ε忡�� cell�� �����͸� string���� ����
     * 
     * @param cell
     * @return
     */
    public static String getStringValue(Cell cell) {
        String rtnValue = "";
        try {
            rtnValue = cell.getStringCellValue();
        } catch(IllegalStateException e) {
            rtnValue = Integer.toString((int)cell.getNumericCellValue());            
        }
        
        return rtnValue;
    }
    
    public static int getIntValue(Cell cell) {
        int rtnValue = 0;
        try {
            rtnValue = (int) cell.getNumericCellValue();
        } catch(IllegalStateException e) {
            rtnValue = 0;            
        }
        
        return rtnValue;
    }



}
