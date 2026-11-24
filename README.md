# Widget Âm lịch Việt Nam

Widget màn hình chính hiển thị ngày âm lịch Việt Nam (UTC+7).
- Chỉ hiển thị, chạm vào không mở app.
- Tính offline, không cần mạng, tự đổi ngày lúc nửa đêm.
- Hiện: ngày dương + thứ, ngày âm (số lớn), tháng âm, can chi năm, và nhãn lễ/Mùng 1/Rằm.

## Build ra APK bằng GitHub (không cần cài gì)

1. Tạo repo mới trên github.com (để Private cũng được).
2. Upload toàn bộ nội dung thư mục này lên repo (giữ nguyên cấu trúc thư mục, nhớ cả thư mục `.github`).
   Cách dễ nhất trên máy tính: giải nén zip, kéo thả tất cả vào trang repo ("Add file" → "Upload files"), rồi Commit.
3. Vào tab **Actions**, workflow "Build APK" sẽ tự chạy (khoảng 3–5 phút). Nếu chưa chạy: chọn workflow → **Run workflow**.
4. Chạy xong (dấu tick xanh), vào mục **Releases** bên phải trang repo, tải file `app-debug.apk` ở bản `latest`.
   (Hoặc vào lần chạy trong Actions, tải artifact `amlich-widget-apk`, giải nén ra file APK.)
5. Mở APK trên điện thoại, cho phép "cài từ nguồn không xác định" nếu máy hỏi.

## Thêm widget lên màn hình

Nhấn giữ chỗ trống trên màn hình chính → Widget → "Âm lịch Việt Nam" → kéo ra. Kéo viền để đổi kích thước.

## Build bằng Android Studio (nếu thích)

Mở thư mục này bằng Android Studio, chờ sync xong, chọn Build → Build APK(s).
