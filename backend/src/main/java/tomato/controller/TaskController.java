package tomato.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tomato.common.ApiResponse;
import tomato.dto.task.TaskCreateRequest;
import tomato.dto.task.TaskUpdateRequest;
import tomato.service.TaskService;
import tomato.vo.task.TaskVO;

import java.util.List;

/**
 * 任务控制器
 */
@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * 获取任务列表
     *
     * @param userId 用户ID
     * @return 任务列表
     */
    @GetMapping("/list")
    public ApiResponse<List<TaskVO>> list(@RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.success(taskService.listByUserId(userId));
    }

    /**
     * 创建任务
     *
     * @param userId  用户ID
     * @param request 创建请求
     * @return 创建的任务
     */
    @PostMapping("/create")
    public ApiResponse<TaskVO> create(@RequestHeader("X-User-Id") Long userId,
                                      @Valid @RequestBody TaskCreateRequest request) {
        return ApiResponse.success(taskService.create(userId, request));
    }

    /**
     * 更新任务
     *
     * @param userId  用户ID
     * @param id      任务ID
     * @param request 更新请求
     * @return 更新后的任务
     */
    @PutMapping("/update/{id}")
    public ApiResponse<TaskVO> update(@RequestHeader("X-User-Id") Long userId,
                                      @PathVariable Long id,
                                      @Valid @RequestBody TaskUpdateRequest request) {
        return ApiResponse.success(taskService.update(userId, id, request));
    }

    /**
     * 删除任务
     *
     * @param userId 用户ID
     * @param id     任务ID
     * @return 成功响应
     */
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> delete(@RequestHeader("X-User-Id") Long userId, @PathVariable Long id) {
        taskService.delete(userId, id);
        return ApiResponse.successMessage("任务删除成功");
    }

    /**
     * 切换任务完成状态
     *
     * @param userId 用户ID
     * @param id     任务ID
     * @return 更新后的任务
     */
    @PutMapping("/finish/{id}")
    public ApiResponse<TaskVO> toggleFinish(@RequestHeader("X-User-Id") Long userId, @PathVariable Long id) {
        return ApiResponse.success(taskService.toggleFinish(userId, id));
    }
}
