document.getElementById("submitBtn").addEventListener("click", function() {
    const concern = document.getElementById("userConcern").value;

    if (!concern) {
        alert("고민을 입력해주세요~ 😉");
        return;
    }

    // 고민 입력 폼 숨기고, 카드 섞는 중 메시지 보여주기
    document.getElementById("concernSection").style.display = 'none';
    document.getElementById("shuffleSection").style.display = 'block';

    //카드 뽑기
    fetch('/card/onecard/draw')
        .then(response => response.json())
        .then(data => {
            console.log(data)
            const cardResult = document.getElementById('resultSection');
            cardResult.innerHTML = `
                        <p>카드명: ${data.cardName}</p>
                        <p>카드종류: ${data.cardImgName}</p>
                        <img src="${data.cardImgUrl}" alt="${data.cardName}"/>
                    `;
        })
        .catch(error => {
            console.error('Error:', error);
        });

    // 일정 시간 후 카드 결과 오픈
    setTimeout(function() {
        document.getElementById("shuffleSection").style.display = 'none';
        document.getElementById("resultSection").style.display = 'block';
        // 카드 섞기
        // document.getElementById("selectSection").style.display = 'block';
    }, 3000); // 3초 후
});

function pickCard() {

}