package semicolon.studentmonitoringapp.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.studentmonitoringapp.data.models.SchoolClass;
import semicolon.studentmonitoringapp.data.models.Teacher;
import semicolon.studentmonitoringapp.data.repositories.SchoolClassRepository;
import semicolon.studentmonitoringapp.data.repositories.TeacherRepository;
import semicolon.studentmonitoringapp.dtos.request.RegisterTeacherRequestDto;
import semicolon.studentmonitoringapp.dtos.response.TeacherRegistrationDetailsDto;
import semicolon.studentmonitoringapp.dtos.response.TeacherResponseDto;
import semicolon.studentmonitoringapp.exceptions.NotFoundException;
import semicolon.studentmonitoringapp.utils.Utility;
import semicolon.studentmonitoringapp.utils.mappers.SchoolClassMapper;
import tools.jackson.databind.ObjectMapper;

import java.time.Clock;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class AdminTeacherServiceImpl implements AdminTeacherService {
    private final TeacherRepository teacherRepository;
    private final SchoolClassMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final SchoolClassRepository schoolClassRepository;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    @Override
    public TeacherRegistrationDetailsDto registerTeacher(RegisterTeacherRequestDto registerRequestDto) {
        return mapRequestToResponse(registerRequestDto);
    }

    @Override
    @Transactional
    public List<TeacherResponseDto> getAllActiveTeachers() {
        List<Teacher> teachers = teacherRepository.findAllByIsActiveTrue();
        return mapTeachersToResponses(teachers);
    }



    @Override
    public List<TeacherResponseDto> getAllTeachers() {
        List<Teacher> teachers = teacherRepository.findAll();
        return mapTeachersToResponses(teachers);
    }

    @Override
    @Transactional//test this specifically
    public UUID removeTeacher(UUID teacherId) {
        Teacher found = getTeacher(teacherId);
        found.setIsActive(false);
        found.setDeletedAt(Instant.now(clock));
        log.info("Removing teacher : {}",
                objectMapper.writeValueAsString(found));
        return found.getId();
    }

    @Override
    @Transactional
    public TeacherResponseDto findTeacher(UUID teacherId){
        Teacher found = getTeacher(teacherId);
        log.info("Getting Teacher: {}",
                objectMapper.writeValueAsString(found));
        return mapTeacherEntityToResponse(found);

    }













    private TeacherResponseDto mapTeacherEntityToResponse(Teacher found) {
        TeacherResponseDto responseDto = mapper.toDto(found);
        Set<SchoolClass> schoolClasses = found.getSchoolClasses();
        responseDto.setSchoolClasses(schoolClasses.stream()
                .map(mapper::toDto).collect(Collectors.toSet()));
        return responseDto;
    }


    private Teacher getTeacher(UUID teacherId) {
        return teacherRepository.findById(teacherId)
                .orElseThrow(()-> new NotFoundException("Teacher profile not found"));
    }


    private List<TeacherResponseDto> mapTeachersToResponses(List<Teacher> teachers) {
        return teachers.stream()
                .map(this::mapTeacherEntityToResponse)
                .toList();
    }

    private TeacherRegistrationDetailsDto mapRequestToResponse(RegisterTeacherRequestDto registerRequestDto) {
        Teacher teacher = mapper.toEntity(registerRequestDto);
        String hashedPassword = passwordEncoder.encode(Utility.generateTeacherPassword());
        teacher.setGeneratedPassword(hashedPassword);
        List<SchoolClass> schoolClasses = schoolClassRepository.findAllById(registerRequestDto.getClassIds());
        teacher.setSchoolClasses(new HashSet<>(schoolClasses));
        Teacher saved = teacherRepository.save(teacher);
        log.info("Saved teacher : {}", saved);
        TeacherRegistrationDetailsDto registrationDetailsDto = new TeacherRegistrationDetailsDto();
        registrationDetailsDto.setGeneratedPassword(teacher.getGeneratedPassword());
        registrationDetailsDto.setEmail(teacher.getEmail());
        return registrationDetailsDto;
    }


}
