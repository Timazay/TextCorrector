package by.timofeyzaytsev.textcorrector.controller.textpolish;

import by.timofeyzaytsev.textcorrector.common.exception.ErrorResponseDto;
import by.timofeyzaytsev.textcorrector.features.textpolish.create_task.CreateTextPolishTaskHandler;
import by.timofeyzaytsev.textcorrector.features.textpolish.create_task.CreateTextPolishTaskRequest;
import by.timofeyzaytsev.textcorrector.features.textpolish.create_task.CreateTextPolishTaskResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/text-polish-tasks")
public class CreateTextPolishTaskController {

    private final CreateTextPolishTaskHandler handler;

    @Operation(
            summary = "Create a new text polishing task",
            description = "Creates a new task for text polishing and returns its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Task successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateTextPolishTaskResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input - validation failed (empty text, less than 3 characters, or no letters)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PostMapping
    public CreateTextPolishTaskResponse createTask(@Valid @RequestBody CreateTextPolishTaskRequest request) {
        return handler.execute(request);
    }
}
