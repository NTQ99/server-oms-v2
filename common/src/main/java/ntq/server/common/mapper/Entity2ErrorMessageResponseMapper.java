package ntq.server.common.mapper;

import ntq.server.common.model.response.ErrorMessageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.messaging.support.ErrorMessage;

@Mapper
public interface Entity2ErrorMessageResponseMapper extends BeanMapper<ErrorMessage, ErrorMessageResponse> {
  Entity2ErrorMessageResponseMapper INSTANCE = Mappers.getMapper(Entity2ErrorMessageResponseMapper.class);
}
