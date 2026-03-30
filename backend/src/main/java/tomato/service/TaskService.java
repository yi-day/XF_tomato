package tomato.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tomato.common.BusinessException;
import tomato.dto.task.TaskCreateRequest;
import tomato.dto.task.TaskUpdateRequest;
import tomato.entity.Task;
import tomato.mapper.TaskMapper;
import tomato.vo.task.TaskVO;

import java.util.List;

/**
 * 任务服务层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;
    private final UserService userService;

    /**
     * 查询用户的所有任务
     *
     * @param userId 用户ID
     * @return 任务列表
     */
    public List<TaskVO> listByUserId(Long userId) {
        log.info("[查询任务列表] userId: {}", userId);
        userService.getUserOrThrow(userId);
        List<TaskVO> tasks = taskMapper.findByUserId(userId).stream().map(this::convertToVO).toList();
        log.info("[查询任务列表] 查询完成, userId: {}, 任务数量: {}", userId, tasks.size());
        return tasks;
    }

    /**
     * 创建任务
     *
     * @param userId  用户ID
     * @param request 创建请求
     * @return 创建的任务
     */
    public TaskVO create(Long userId, TaskCreateRequest request) {
        log.info("[创建任务] userId: {}, title: {}, priority: {}, estimatedPomodoros: {}", 
                userId, request.getTitle(), request.getPriority(), request.getEstimatedPomodoros());
        userService.getUserOrThrow(userId);
        Task task = new Task();
        task.setUserId(userId);
        task.setTitle(request.getTitle());
        task.setPriority(request.getPriority());
        task.setEstimatedPomodoros(request.getEstimatedPomodoros());
        task.setDeadline(request.getDeadline());
        task.setCompleted(Boolean.FALSE);
        taskMapper.insert(task);
        log.info("[创建任务] 创建成功, taskId: {}, userId: {}", task.getId(), userId);
        return convertToVO(requireTask(userId, task.getId()));
    }

    /**
     * 更新任务
     *
     * @param userId  用户ID
     * @param id      任务ID
     * @param request 更新请求
     * @return 更新后的任务
     */
    public TaskVO update(Long userId, Long id, TaskUpdateRequest request) {
        log.info("[更新任务] userId: {}, taskId: {}, title: {}", userId, id, request.getTitle());
        Task task = requireTask(userId, id);
        task.setTitle(request.getTitle());
        task.setPriority(request.getPriority());
        task.setEstimatedPomodoros(request.getEstimatedPomodoros());
        task.setDeadline(request.getDeadline());
        taskMapper.update(task);
        log.info("[更新任务] 更新成功, taskId: {}", id);
        return convertToVO(requireTask(userId, id));
    }

    /**
     * 删除任务
     *
     * @param userId 用户ID
     * @param id     任务ID
     */
    public void delete(Long userId, Long id) {
        log.info("[删除任务] userId: {}, taskId: {}", userId, id);
        Task task = requireTask(userId, id);
        taskMapper.delete(task.getId(), userId);
        log.info("[删除任务] 删除成功, taskId: {}", id);
    }

    /**
     * 切换任务完成状态
     *
     * @param userId 用户ID
     * @param id     任务ID
     * @return 更新后的任务
     */
    public TaskVO toggleFinish(Long userId, Long id) {
        Task task = requireTask(userId, id);
        boolean newStatus = !Boolean.TRUE.equals(task.getCompleted());
        log.info("[切换任务状态] userId: {}, taskId: {}, 新状态: {}", userId, id, newStatus ? "已完成" : "未完成");
        task.setCompleted(newStatus);
        taskMapper.updateCompleted(task);
        return convertToVO(requireTask(userId, id));
    }

    /**
     * 统计用户已完成任务数
     *
     * @param userId 用户ID
     * @return 已完成任务数
     */
    public Integer countCompleted(Long userId) {
        Integer value = taskMapper.countCompletedByUserId(userId);
        return value == null ? 0 : value;
    }

    /**
     * 统计用户任务总数
     *
     * @param userId 用户ID
     * @return 任务总数
     */
    public Integer countTotal(Long userId) {
        Integer value = taskMapper.countByUserId(userId);
        return value == null ? 0 : value;
    }

    /**
     * 获取任务，不存在则抛出异常
     *
     * @param userId 用户ID
     * @param id     任务ID
     * @return 任务实体
     */
    public Task requireTask(Long userId, Long id) {
        Task task = taskMapper.findByIdAndUserId(id, userId);
        if (task == null) {
            throw new BusinessException(404, "任务不存在");
        }
        return task;
    }

    /**
     * 转换为任务视图对象
     *
     * @param task 任务实体
     * @return 任务视图对象
     */
    private TaskVO convertToVO(Task task) {
        TaskVO taskVO = new TaskVO();
        taskVO.setId(task.getId());
        taskVO.setTitle(task.getTitle());
        taskVO.setPriority(task.getPriority());
        taskVO.setEstimatedPomodoros(task.getEstimatedPomodoros());
        taskVO.setDeadline(task.getDeadline());
        taskVO.setCompleted(task.getCompleted());
        return taskVO;
    }
}
