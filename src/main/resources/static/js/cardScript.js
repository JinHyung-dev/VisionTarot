document.getElementById("submitBtn").addEventListener("click", function() {
    const concern = document.getElementById("userConcern").value;

    if (!concern) {
        alert("고민을 입력해주세요~ 😉");
        return;
    }

    // 고민 입력 폼 숨기고, 카드 섞는 중 메시지 보여주기
    document.getElementById("concernSection").style.display = 'none';
    document.getElementById("shuffleSection").style.display = 'flex';

    //카드 뽑기
    fetch('/card/onecard/draw-with-analyze', {
        method: 'POST',
        headers: {
            'Content-Type': 'text/plain'
        },
        body: concern
        })
    .then(response => response.json())
    .then(data => {
        const cardResult = document.getElementById('result');
        cardResult.innerHTML = `
                    <p>카드명: ${data.card.cardName}</p>
                    <p>카드종류: ${data.card.cardImgName}</p>
                    <img src="${data.card.cardImgUrl}" alt="${data.card.cardName}" id="card-img" width="150px"/>
                `;
        const reviewContent = document.getElementById('describe-content');
        if(data.card.reversed) {
            reviewContent.innerText = `${concern}라는 고민에 대한 유니버셜 타로카드의 결과는 ${data.card.cardName} 역방향입니다.`;
            document.getElementById("card-img").style.transform = "scaleY(-1)";
        } else {
            reviewContent.innerText = `"${concern}"라는 고민에 대한 유니버셜 타로카드의 결과는 ${data.card.cardName} 정방향입니다.`;
        }
        reviewContent.innerText += ` ${data.geminiAnswer}`;
    })
    .catch(error => {
        console.error('Error:', error);
    });

    // 일정 시간 후 카드 결과 오픈
    setTimeout(function() {
        document.getElementById("shuffleSection").style.display = 'none';
        document.body.lastElementChild.remove();
        document.getElementById("resultSection").style.display = 'block';
        // 카드 섞기
        // document.getElementById("selectSection").style.display = 'block';
    }, 3000); // 3초 후
});

document.getElementById("clipboard-btn").addEventListener("click", function () {
    const textToCopy = document.getElementById("describe-content").innerText;

    if (navigator.clipboard) {
        // 클립보드에 텍스트 복사
        navigator.clipboard
            .writeText(textToCopy)
            .then(() => {
                alert("텍스트가 클립보드에 복사되었습니다!");
            })
            .catch((err) => {
                console.error("복사 실패:", err);
                alert("복사에 실패했습니다!");
            });
    } else {
        const clipboard = new ClipboardJS('#clipboard-btn', {
            text: function() {
                return textToCopy;
            }
        });

        clipboard.on('success', function(e) {
            alert('텍스트가 클립보드에 복사되었습니다!');
        });

        clipboard.on('error', function(e) {
            console.error("복사 실패:", err);
            alert('복사에 실패했습니다!');
        });
    }
});

document.getElementById("check-btn").addEventListener("click", function() {
    let summary = document.getElementById('describe-content').innerText;

    fetch('/concern-card/create',{
        method: 'POST',
        headers: {
            'Content-Type': 'text/plain'
        },
        body: summary
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            const concernBox = document.getElementById('concern-card-box');
            concernBox.innerHTML = `
            <img src="${data.concernCardImageUrl}" alt="concern-card" id="concern-card-img" width="320px"/>
        `;
            document.getElementById("describe").style.display = 'block';
        }).catch(error => {
        console.error('Error:', error);
    });
});