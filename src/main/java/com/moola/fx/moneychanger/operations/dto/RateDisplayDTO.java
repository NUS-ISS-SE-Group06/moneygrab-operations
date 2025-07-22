package com.moola.fx.moneychanger.operations.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * API response wrapper carrying the board style plus the list of rate rows.
 */
@Getter
@Setter
@AllArgsConstructor
public class RateDisplayDTO {

    /** Display layout chosen by the money-changer (e.g. “Extended Monitor Style”). */
    private String style;

    /** All rates for the current money-changer. */
    private List<RateDTO> rates;

}
