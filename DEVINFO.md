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
  * Version 1
    * 최초 버전
  * Version 2
    * MainActivity의 FloatingActionButton 제거
    * MapFragment에 FloatingActionButton 추가
    * 한손 조작 편의성을 위해 Control bar를 아래로 배치
  * Version 3
    * 검색 버튼과 내 위치 버튼의 위치를 교환
    * '좋아요'한 장소가 없을 경우 알림 메시지 출력
  * Version 4
    * 검색 버튼 삭제 및 검색바 추가
    * SearchActivity로 전환 시 원형 애니메이션 시작 위치 변경

<br>

### 3. 데이터



