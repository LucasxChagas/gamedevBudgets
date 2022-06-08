package com.lucasxchagas.gamedevbudgets.service.dto;

import com.lucasxchagas.gamedevbudgets.domain.enumeration.GameGender;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.lucasxchagas.gamedevbudgets.domain.Game} entity.
 */
@Schema(description = "The Employee entity.")
public class GameDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Lob
    private byte[] thumbnail;

    private String thumbnailContentType;

    @Lob
    private String description;

    private GameGender gender;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnailContentType() {
        return thumbnailContentType;
    }

    public void setThumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GameGender getGender() {
        return gender;
    }

    public void setGender(GameGender gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameDTO)) {
            return false;
        }

        GameDTO gameDTO = (GameDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gameDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", thumbnail='" + getThumbnail() + "'" +
            ", description='" + getDescription() + "'" +
            ", gender='" + getGender() + "'" +
            "}";
    }
}
