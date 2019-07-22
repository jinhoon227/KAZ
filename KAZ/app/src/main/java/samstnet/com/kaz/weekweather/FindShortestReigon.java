package samstnet.com.kaz.weekweather;

import android.util.FloatMath;
import android.util.Log;

// 주간 날씨 파싱은 20개 가량의 지역으로 밖에 안나타나기 때문에
// 현재 좌표에서 가장 가까운 지역을 찾기위한 함수 작성
public class FindShortestReigon {
    CityCode[] cityCode = new CityCode[16];

    FindShortestReigon(){
        cityCode[0] = new CityCode(); cityCode[0].mCityCode = "춘천"; cityCode[0].mlat = 37.881288; cityCode[0].mlon = 127.730082;
        cityCode[1] = new CityCode(); cityCode[1].mCityCode = "백령도"; cityCode[1].mlat = 37.950966; cityCode[1].mlon = 124.656029;
        cityCode[2] = new CityCode(); cityCode[2].mCityCode = "강릉"; cityCode[2].mlat = 37.751853; cityCode[2].mlon = 128.876058;
        cityCode[3] = new CityCode(); cityCode[3].mCityCode = "서울"; cityCode[3].mlat = 37.564341; cityCode[3].mlon = 126.975609;
        cityCode[4] = new CityCode(); cityCode[4].mCityCode = "인천"; cityCode[4].mlat = 37.455682; cityCode[4].mlon = 126.704472;
        cityCode[5] = new CityCode(); cityCode[5].mCityCode = "울릉도"; cityCode[5].mlat = 37.506368; cityCode[5].mlon = 130.857154;
        cityCode[6] = new CityCode(); cityCode[6].mCityCode = "수원"; cityCode[6].mlat = 37.263406; cityCode[6].mlon = 127.028584;
        cityCode[7] = new CityCode(); cityCode[7].mCityCode = "청주"; cityCode[7].mlat = 36.641879; cityCode[7].mlon = 127.488752;
        cityCode[8] = new CityCode(); cityCode[8].mCityCode = "대전"; cityCode[8].mlat = 36.350412; cityCode[8].mlon = 127.384547;
        cityCode[9] = new CityCode(); cityCode[9].mCityCode = "대구"; cityCode[9].mlat = 35.871436; cityCode[9].mlon = 128.601445;
        cityCode[10] = new CityCode(); cityCode[10].mCityCode = "전주"; cityCode[10].mlat = 35.824193; cityCode[10].mlon = 127.148000;
        cityCode[11] = new CityCode(); cityCode[11].mCityCode = "울산"; cityCode[11].mlat = 35.538739; cityCode[11].mlon = 129.311360;
        cityCode[12] = new CityCode(); cityCode[12].mCityCode = "마산"; cityCode[12].mlat = 35.213516; cityCode[12].mlon = 128.581433;
        cityCode[13] = new CityCode(); cityCode[13].mCityCode = "광주"; cityCode[13].mlat = 35.160073; cityCode[13].mlon = 126.851434;
        cityCode[14] = new CityCode(); cityCode[14].mCityCode = "부산"; cityCode[14].mlat = 35.179555; cityCode[14].mlon = 129.075642;
        cityCode[15] = new CityCode(); cityCode[15].mCityCode = "제주"; cityCode[15].mlat = 33.499597; cityCode[15].mlon = 126.531254;
    }

    //거리계산 함수
    private float spacing(CityCode cityCode, Double[] target)
    {
        float x = (float)(cityCode.mlat - target[0]);
        float y = (float)(cityCode.mlon - target[1]);
        return (float)Math.sqrt(x * x + y * y);
    }

    //최소거리 지역 반환 함수
    public String ShortestDistance(Double[] target){
        float spacing[] = new float[cityCode.length];
        for(int i = 0 ; i < cityCode.length ; i++)
        { spacing[i] = spacing(cityCode[i], target); }

        float shortSpacing = spacing[0];
        int shortIndex = -1;
        for(int i = 0; i < spacing.length ; i++)
        { //설마 같은거리가 나올려고?
            if(shortSpacing >= spacing[i])
            {
                shortSpacing = spacing[i];
                shortIndex = i;
            }
        }
        String nearCityCode = cityCode[shortIndex].mCityCode;
        return nearCityCode;
    }

}


