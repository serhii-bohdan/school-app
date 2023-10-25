package ua.foxminded.schoolapp.dto.mapper;

import org.modelmapper.ModelMapper;
import ua.foxminded.schoolapp.dto.GroupDto;
import ua.foxminded.schoolapp.model.Group;

/**
 * The GroupMapper class is responsible for mapping between {@link Group} and
 * {@link GroupDto} objects. It uses the {@link ModelMapper} library to perform
 * the mapping.
 * </p>
 *
 * @author Serhii Bohdan
 */
public class GroupMapper {

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    private GroupMapper() {
    }

    /**
     * Maps a {@link Group} object to a {@link GroupDto} object.
     *
     * @param group the Group object to map
     * @return the mapped GroupDto object
     */
    public static GroupDto mapGroupToDto(Group group) {
        return MODEL_MAPPER.map(group, GroupDto.class);
    }

    /**
     * Maps a {@link GroupDto} object to a {@link Group} object.
     *
     * @param groupDto the GroupDto object to map
     * @return the mapped Group object
     */
    public static Group mapDtoToGroup(GroupDto groupDto) {
        return MODEL_MAPPER.map(groupDto, Group.class);
    }

}
