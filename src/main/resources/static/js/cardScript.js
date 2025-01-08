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
            const cardResult = document.getElementById('result');
            cardResult.innerHTML = `
                        <p>카드명: ${data.cardName}</p>
                        <p>카드종류: ${data.cardImgName}</p>
                        <img src="${data.cardImgUrl}" alt="${data.cardName}" width="150px"/>
                    `;

            const reviewContent = document.getElementById('describe-content');
            if(data.reversed) {
               reviewContent.innerHTML = `"${concern}"라는 고민에 대한 유니버셜 타로카드의 결과는 ${data.cardName} 역방향입니다.`;
            } else {
               reviewContent.innerHTML = `"${concern}"라는 고민에 대한 유니버셜 타로카드의 결과는 ${data.cardName} 정방향입니다.`;
            }
            document.getElementById("describe").style.display = 'block';
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