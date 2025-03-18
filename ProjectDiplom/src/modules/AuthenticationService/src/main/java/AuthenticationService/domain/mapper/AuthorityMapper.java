package AuthenticationService.domain.mapper;

import AuthenticationService.domain.dto.AuthorityDto;
import AuthenticationService.domain.model.Authority;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthorityMapper {
    AuthorityDto map(Authority authority);
}
