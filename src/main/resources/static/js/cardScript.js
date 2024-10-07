document.getElementById("submitBtn").addEventListener("click", function() {
    const concern = document.getElementById("userConcern").value;

    if (!concern) {
        alert("고민을 입력해주세요~ 😉");
        return;
    }

    // 고민 입력 폼 숨기고, 카드 섞는 중 메시지 보여주기
    document.getElementById("concernSection").style.display = 'none';
    document.getElementById("shuffleSection").style.display = 'block';

    // 일정 시간 후 카드 선택 섹션 보여주기
    setTimeout(function() {
        document.getElementById("shuffleSection").style.display = 'none';
        document.getElementById("selectSection").style.display = 'block';
    }, 3000); // 3초 후에 카드 선택 화면 보여주기
});
