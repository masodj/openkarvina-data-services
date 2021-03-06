package cz.technecium.openkarvinadataservices.domain;

import lombok.Getter;
import lombok.Setter;

/**
 *
 *
 * @author Martin.Sobek
 */
@Getter
@Setter
public class Player {

    private PlayerIdentifier playerIdentifier;
    private String name;
    private String club;
    private Integer fideRating;
    private Integer czRating;
    private String title;
    private String federation;
    private String birthday;
}
