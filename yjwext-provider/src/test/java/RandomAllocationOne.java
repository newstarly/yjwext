import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 随机分配考官-->教室(考官和教室个数相同)
 */
public class RandomAllocationOne {
    private final static int EXAMINERTIMES = 50;
    private final static int ROOMTIMES = 50;

    public static void main(String args[]) {
        String[] examiners = {"考官1", "考官2", "考官3", "考官4", "考官5", "考官6", "考官7", "考官8", "考官9", "考官10"};
        List<String> examinerList = new ArrayList<>();
        for (String examiner : examiners) {
            examinerList.add(examiner);
        }

        //多次打乱考官顺序
        for (int k = 0; k < EXAMINERTIMES; k++) {
            Collections.shuffle(examinerList);
        }

        //假如一个教室对应一个考官，把考官分配到不同的教室
        String[] rooms = {"教室1", "教室2", "教室3", "教室4", "教室5", "教室6", "教室7", "教室8", "教室9", "教室10"};
        List<String> roomList = new ArrayList<>();
        for (String room : rooms) {
            roomList.add(room);
        }

        //打乱所有教室的顺序
        for (int k = 0; k < ROOMTIMES; k++) {
            Collections.shuffle(roomList);
        }

        //考官分配到教室
        for (int i = 0; i < examinerList.size(); i++) {
            String examiner = examinerList.get(i);
            //根据生成的随机数，获取哪一个教室 对教室分配考官
            String room = roomList.get(i);
            System.out.println("考官:" + examiner + ",分配的教室:" + room);
        }
    }
}
