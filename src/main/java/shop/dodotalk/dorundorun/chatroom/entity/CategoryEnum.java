package shop.dodotalk.dorundorun.chatroom.entity;


/* 방 카테고리 */
public enum CategoryEnum {
    STUDY("공부"),
    SOCIAL("친목"),
    HOBBY("취미"),
    WORKOUT("운동"),
    JOBS("직장인"),
    INVESTMENT("재테크"),
    ETC("기타");


    final private String categoryKr;

    private CategoryEnum(String category) {
        this.categoryKr = category;
    }

    public String getCategoryKr() {
        return this.categoryKr;
    }



}
