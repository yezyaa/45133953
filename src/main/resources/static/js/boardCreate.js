document.addEventListener('DOMContentLoaded', function () {
    const fileInputsContainer = document.getElementById('fileInputs');
    const maxFileInputs = 5; // 최대 파일 입력 필드 개수 제한

    // 파일 입력 필드 동적 추가
    fileInputsContainer.addEventListener('change', function (event) {
        const target = event.target;

        // 파일이 선택된 경우
        if (target.classList.contains('file-input') && target.files.length > 0) {
            const currentFileInputs = fileInputsContainer.querySelectorAll('.file-input');

            // 최대 개수 제한 확인
            if (currentFileInputs.length >= maxFileInputs) {
                alert(`첨부파일은 최대 ${maxFileInputs}개까지 추가할 수 있습니다.`);
                return;
            }

            // 새로운 파일 입력 필드 추가
            const newInput = document.createElement('input');
            newInput.type = 'file';
            newInput.name = 'attachments';
            newInput.className = 'file-input';
            fileInputsContainer.appendChild(newInput);
        }
    });
});
