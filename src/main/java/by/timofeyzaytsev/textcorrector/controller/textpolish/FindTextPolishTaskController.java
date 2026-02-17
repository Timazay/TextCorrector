package by.timofeyzaytsev.textcorrector.controller.textpolish;

import by.timofeyzaytsev.textcorrector.common.exception.ErrorResponseDto;
import by.timofeyzaytsev.textcorrector.features.textpolish.find_task.FindTextPolishTaskHandler;
import by.timofeyzaytsev.textcorrector.features.textpolish.find_task.FindTextPolishTaskResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/text-polish-tasks")
public class FindTextPolishTaskController {

    private final FindTextPolishTaskHandler handler;

    @GetMapping("/{taskId}")
    @Operation(
            summary = "Find text polishing task by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Task found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FindTextPolishTaskResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid task ID format",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    public FindTextPolishTaskResponse findTextPolishTask(@PathVariable UUID taskId) {
        return handler.execute(taskId);
    }
}
