package semicolon.studentmonitoringapp.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.studentmonitoringapp.data.models.SchoolClass;
import semicolon.studentmonitoringapp.data.models.Student;
import semicolon.studentmonitoringapp.data.models.Teacher;
import semicolon.studentmonitoringapp.data.repositories.SchoolClassRepository;
import semicolon.studentmonitoringapp.data.repositories.StudentRepository;
import semicolon.studentmonitoringapp.data.repositories.TeacherRepository;
import semicolon.studentmonitoringapp.dtos.request.CreateClassRequestDto;
import semicolon.studentmonitoringapp.dtos.request.SchoolClassPatchRequestDto;
import semicolon.studentmonitoringapp.dtos.response.ClassResponseDto;
import semicolon.studentmonitoringapp.dtos.response.StudentResponseDto;
import semicolon.studentmonitoringapp.dtos.response.TeacherResponseDto;
import semicolon.studentmonitoringapp.exceptions.NotFoundException;
import semicolon.studentmonitoringapp.exceptions.SchoolClassDuplicateException;
import semicolon.studentmonitoringapp.utils.mappers.SchoolClassMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AdminClassServiceImpl implements AdminClassService {
    private final SchoolClassRepository classRepository;
    private final SchoolClassMapper schoolClassMapper;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;


    @Override
    public void createClass(CreateClassRequestDto classRequestDto) {
        SchoolClass schoolClass = schoolClassMapper.toEntity(classRequestDto);
        checkForDuplicate(classRequestDto, schoolClass);
        List<Teacher> teachers = teacherRepository.findAllById(classRequestDto.getTeacherIds());
        List<Student> students = studentRepository.findAllById(classRequestDto.getStudentIds());
        schoolClass.setTeachers(new HashSet<>(teachers));
        schoolClass.setStudents(new HashSet<>(students));
        classRepository.save(schoolClass);
    }

    @Override
    public List<ClassResponseDto> findAllSchoolClasses() {
        List<SchoolClass> classes = classRepository.findAll();
        return getClassResponseDtos(classes);
    }

    @Override
    @Transactional
    public void updateClass(UUID classId, SchoolClassPatchRequestDto classPatchDto) {
        SchoolClass schoolClass = classRepository.findById(classId)
                        .orElseThrow(()-> new NotFoundException("Class Not Found"));
        teacherRepository.findAllById(classPatchDto.getTeachers())
                        .forEach(schoolClass::addTeacher);
        studentRepository.findAllById(classPatchDto.getStudents())
                        .forEach(schoolClass::addStudent);
        classRepository.save(schoolClass);
    }


    private List<ClassResponseDto> getClassResponseDtos(List<SchoolClass> classes) {
        return classes.stream()
                .map(this::populateClassResponseDto)
                .toList();
    }

    private ClassResponseDto populateClassResponseDto(SchoolClass schoolClass) {
        ClassResponseDto responseDto = schoolClassMapper.toDto(schoolClass);
        List<TeacherResponseDto> teachers = schoolClass.getTeachers().stream().map(schoolClassMapper::toDto).toList();
        List<StudentResponseDto> students = schoolClass.getStudents().stream().map(schoolClassMapper::toDto).toList();
        responseDto.setTeachers(teachers);
        responseDto.setStudents(students);
        return responseDto;
    }


    private void checkForDuplicate(CreateClassRequestDto classRequestDto, SchoolClass schoolClass) {
        if(classRepository.existsByNameIgnoreCaseAndAcademicYear(schoolClass.getName(), schoolClass.getAcademicYear())) {
            throw new SchoolClassDuplicateException(classRequestDto);
        }
    }
}
