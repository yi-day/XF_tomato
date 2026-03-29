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

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/list")
    public ApiResponse<List<TaskVO>> list(@RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.success(taskService.listByUserId(userId));
    }

    @PostMapping("/create")
    public ApiResponse<TaskVO> create(@RequestHeader("X-User-Id") Long userId,
                                      @Valid @RequestBody TaskCreateRequest request) {
        return ApiResponse.success(taskService.create(userId, request));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<TaskVO> update(@RequestHeader("X-User-Id") Long userId,
                                      @PathVariable Long id,
                                      @Valid @RequestBody TaskUpdateRequest request) {
        return ApiResponse.success(taskService.update(userId, id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> delete(@RequestHeader("X-User-Id") Long userId, @PathVariable Long id) {
        taskService.delete(userId, id);
        return ApiResponse.successMessage("任务删除成功");
    }

    @PutMapping("/finish/{id}")
    public ApiResponse<TaskVO> toggleFinish(@RequestHeader("X-User-Id") Long userId, @PathVariable Long id) {
        return ApiResponse.success(taskService.toggleFinish(userId, id));
    }
}
