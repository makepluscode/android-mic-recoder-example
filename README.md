# android-mic-recoder-example
This Android program records audio using `MediaRecorder`, saves it to the device's storage with `MediaStore`, and allows users to start and stop recording through a simple user interface.

## 주요 기능
- 오디오 녹음 시작 및 중지
- 자동으로 파일명 생성 및 저장

## 사용 방법
1. 애플리케이션을 시작합니다.
2. '녹음 시작' 버튼을 눌러 오디오 녹음을 시작합니다.
3. '녹음 중지' 버튼을 눌러 녹음을 중지하고 파일을 저장합니다.

## 코드 설명
이 애플리케이션은 `MainActivity` 클래스를 포함합니다. 이 클래스에서는 녹음을 시작하고 중지하는 버튼을 관리하며, `MediaRecorder`를 사용하여 오디오 녹음을 수행합니다. 녹음된 오디오 파일은 `MediaStore`를 통해 사용자의 장치에 저장됩니다.
