## 개발 내용

### 1. 지도 업데이트
* 오픈스트리트맵의 학교 지도에 최신 정보가 적용되어 있지 않아 우선 정보를 업데이트했습니다.
<br><img src="/res/dev/img_01.png" width="720px"/>
* 업데이트 전, 후 지도 비교
<br><img src="/res/dev/img_02.png" width="180px"/> <img src="/res/dev/img_03.png" width="180px"/>
<br>

### 2. 앱 디자인
* HallymMap은 '지도', '좋아요', '시설', '더보기'의 4가지 탭으로 이루어져 있습니다.
* 화면은 MainActivity에 BottomNavigationBar을 통해 각 탭별 Fragment를 전환하도록 되어 있습니다.
<br><br><img src="/res/dev/img_04-1.png" width="720px"/>

* 이후 4번의 큰 디자인 변경을 통해 현재와 같은 디자인을 갖추게 되었습니다.
<br><br><img src="/res/dev/img_05-1.png" width="720px"/>
  * `Version 1`
    * 최초 버전
  * `Version 2`
    * MainActivity의 FloatingActionButton 제거
    * MapFragment에 FloatingActionButton 추가
    * 한손 조작 편의성을 위해 Control bar를 아래로 배치
  * `Version 3`
    * 검색 버튼과 내 위치 버튼의 위치를 교환
    * '좋아요'한 장소가 없을 경우 알림 메시지 출력
  * `Version 4`
    * 검색 버튼 삭제 및 검색바 추가
    * SearchActivity로 전환 시 원형 애니메이션 시작 위치 변경

<br>

### 3. 데이터
* 건물과 시설 데이터는 앱 내에 json으로 저장된 데이터를 기본으로 사용합니다.
```json
{
    "floor": 1,
    "type": "STUDY_ROOM",
    "buildingNo": 1,
    "en": "SW Village",
    "id": 30519,
    "kr": "소프트웨어 빌리지",
    "latitude": 37.88643,
    "longitude": 127.73589,
    "name": "소프트웨어 빌리지",
    "searchTag": "공학관,소프트웨어,software,sw"
  }
```
* 그러나 지도 데이터가 업데이트 될 때마다 앱을 업데이트하는 것은 비효율적이므로, SpreadSheet를 이용하여 최신 정보를 유지합니다.
* 셀룰러 데이터 사용과 앱의 부하를 최소화하기 위해 앱 접속시 매번 업데이트를 진행하지 않습니다. 따라서 최신 데이터가 표시되기까지 최대 이틀이 걸릴 수도 있습니다.
* SpreadSheet의 데이터는 [Jsoup](https://jsoup.org)으로 파싱합니다.
```kotlin
const val URL = "..." // SpreadSheet web URL

// Jsoup으로 파싱
Jsoup.connect(URL).get()
```

