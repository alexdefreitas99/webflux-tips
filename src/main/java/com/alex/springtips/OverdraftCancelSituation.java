package com.alex.springtips;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OverdraftCancelSituation {
    PENDING_PAYMENTS("Pagamentos pendentes"),
    WAITING_DEBIT_DAY("Aguardando dia de débito"),
    RELEASED_TO_CANCEL("Liberado para cancelar");

    private final String description;

    OverdraftCancelSituation(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return this.name();
    }
}
