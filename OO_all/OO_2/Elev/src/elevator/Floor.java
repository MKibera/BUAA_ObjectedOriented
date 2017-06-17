package elevator;

public class Floor {//
	private int maxFloor, minFloor;
	Floor(int min_f,int max_f){
		minFloor = min_f;
		maxFloor = max_f;
	}
	int getMin(){//得到楼层允许的最大值
		return minFloor;
	}
	int getMax(){//得到楼层允许的最小值
		return maxFloor;
	}
}
