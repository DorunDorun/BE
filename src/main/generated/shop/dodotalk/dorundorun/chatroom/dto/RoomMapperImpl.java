package shop.dodotalk.dorundorun.chatroom.dto;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import shop.dodotalk.dorundorun.chatroom.dto.response.ChatRoomResponseDto;
import shop.dodotalk.dorundorun.chatroom.entity.Room;
import shop.dodotalk.dorundorun.chatroom.entity.RoomUsers;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-17T01:05:35+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.17 (Oracle Corporation)"
)
@Component
public class RoomMapperImpl implements RoomMapper {

    @Override
    public List<ChatRoomResponseDto> roomsToRoomResponseDtos(List<Room> rooms) {
        if ( rooms == null ) {
            return null;
        }

        List<ChatRoomResponseDto> list = new ArrayList<ChatRoomResponseDto>( rooms.size() );
        for ( Room room : rooms ) {
            list.add( roomToChatRoomResponseDto( room ) );
        }

        return list;
    }

    protected ChatRoomResponseDto roomToChatRoomResponseDto(Room room) {
        if ( room == null ) {
            return null;
        }

        ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto();

        chatRoomResponseDto.setCategory( room.getCategory() );
        chatRoomResponseDto.setSessionId( room.getSessionId() );
        chatRoomResponseDto.setTitle( room.getTitle() );
        chatRoomResponseDto.setSubtitle( room.getSubtitle() );
        chatRoomResponseDto.setStatus( room.isStatus() );
        chatRoomResponseDto.setButtonImage( room.getButtonImage() );
        chatRoomResponseDto.setPassword( room.getPassword() );
        chatRoomResponseDto.setMaster( room.getMaster() );
        chatRoomResponseDto.setSaying( room.getSaying() );
        List<RoomUsers> list = room.getRoomUsers();
        if ( list != null ) {
            chatRoomResponseDto.setRoomUsers( new ArrayList<RoomUsers>( list ) );
        }
        chatRoomResponseDto.setCntUser( room.getCntUser() );

        return chatRoomResponseDto;
    }
}
