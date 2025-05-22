package com.pigma.harusari.task.schedule.command.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompletionStatusUpdateRequest {

    @NotNull
    private Boolean completionStatus;
}