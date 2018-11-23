package nkn6294.java.test;

import java.util.List;

/***
 * Xử lý một giá trị kiểu cơ bản.
 */
public class MonitorValue{
    /***
     * Khởi tạo mặc định. Override lại các phương thức xử lý phù hợp.
     */
    public MonitorValue(){
    }

    /***
     * Xử lý giá trị đưa vào kiểu <i>long</i>.
     *
     * @param length
     */
    public void Action(long value){
    }

    /***
     * Xử lý giá trị kiểu <i>int</i>
     *
     * @param value
     */
    public void Action(int value){
    }

    /***
     * Xử lý giá trị kiểu <i>String</i>
     *
     * @param value
     */
    public void Action(String value){
    }

    /***
     * Xử lý giá trị kiểu <i>double</i>
     *
     * @param value
     */
    public void Action(double value){
    }

    /***
     * Xử lý giá trị kiểu <i>float</i>
     *
     * @param value
     */
    public void Action(float value){
    }

    /***
     * Xử lý giá trị kiểu <i>char</i>
     *
     * @param value
     */
    public void Action(char value){
    }

    /***
     * Xử lý giá trị kiểu <i>short</i>
     *
     * @param value
     */
    public void Action(short value){
    }

    /***
     * Xử lý giá trị kiểu <i>boolean</i>
     *
     * @param value
     */
    public void Action(boolean value){
    }

    /***
     * Xử lý đối tượng kiểu <i>T</i>
     *
     * @param value
     */
    public <T extends Object> void Action(T value){
    }

    /***
     * Xử lý mảng kiểu <i>T</i>
     *
     * @param value
     */
    public <T extends Object> void Action(T[] value){
    }

    /***
     * Xử lý <i>List</i> kiểu <i>T</i>
     *
     * @param value
     */
    public <T extends Object> void Action(List<T> value){
    }
}
