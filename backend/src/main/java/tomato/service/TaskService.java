package tomato.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tomato.common.BusinessException;
import tomato.dto.task.TaskCreateRequest;
import tomato.dto.task.TaskUpdateRequest;
import tomato.entity.Task;
import tomato.mapper.TaskMapper;
import tomato.vo.task.TaskVO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;
    private final UserService userService;

    public List<TaskVO> listByUserId(Long userId) {
        userService.getUserOrThrow(userId);
        return taskMapper.findByUserId(userId).stream().map(this::convertToVO).toList();
    }

    public TaskVO create(Long userId, TaskCreateRequest request) {
        userService.getUserOrThrow(userId);
        Task task = new Task();
        task.setUserId(userId);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setExpectedFocusMinutes(defaultExpectedMinutes(request.getExpectedFocusMinutes()));
        task.setFinished(Boolean.FALSE);
        taskMapper.insert(task);
        return convertToVO(requireTask(userId, task.getId()));
    }

    public TaskVO update(Long userId, Long id, TaskUpdateRequest request) {
        Task task = requireTask(userId, id);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setExpectedFocusMinutes(defaultExpectedMinutes(request.getExpectedFocusMinutes()));
        taskMapper.update(task);
        return convertToVO(requireTask(userId, id));
    }

    public void delete(Long userId, Long id) {
        Task task = requireTask(userId, id);
        taskMapper.delete(task.getId(), userId);
    }

    public TaskVO toggleFinish(Long userId, Long id) {
        Task task = requireTask(userId, id);
        task.setFinished(!Boolean.TRUE.equals(task.getFinished()));
        taskMapper.updateFinished(task);
        return convertToVO(requireTask(userId, id));
    }

    public Integer countCompleted(Long userId) {
        Integer value = taskMapper.countFinishedByUserId(userId);
        return value == null ? 0 : value;
    }

    public Task requireTask(Long userId, Long id) {
        Task task = taskMapper.findByIdAndUserId(id, userId);
        if (task == null) {
            throw new BusinessException(404, "任务不存在");
        }
        return task;
    }

    private Integer defaultExpectedMinutes(Integer expectedFocusMinutes) {
        return expectedFocusMinutes == null ? 25 : expectedFocusMinutes;
    }

    private TaskVO convertToVO(Task task) {
        TaskVO taskVO = new TaskVO();
        taskVO.setId(task.getId());
        taskVO.setTitle(task.getTitle());
        taskVO.setDescription(task.getDescription());
        taskVO.setExpectedFocusMinutes(task.getExpectedFocusMinutes());
        taskVO.setFinished(task.getFinished());
        taskVO.setCreatedAt(task.getCreatedAt());
        taskVO.setUpdatedAt(task.getUpdatedAt());
        return taskVO;
    }
}
