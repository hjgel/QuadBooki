package kobuki.quadbooki.repository;

import kobuki.quadbooki.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserId(String userId);

    Optional<User> findByUserId(String userId);
    // 회원 중복 가입 방지
    Optional<User> findByUserIdAndPhoneNumber(String userId,String phoneNumber);

    // 회원 조회 - 모든 회원 정보 조회
    List<User> findAll();

    // 회원 수정 - 전화번호만 변경 가능
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.phoneNumber = :phoneNumber WHERE u.userId = :userId")
    int updatePhoneNumber(@Param("userId") String userId, @Param("phoneNumber") String phoneNumber);

    // 회원 탈퇴 - 도서를 대여 중인 경우 탈퇴 불가
    Optional<User> findByUserIdAndUserRentCount(String userId, int userRentCount);

    // 로그인 - pw와 id로 로그인
    Optional<User> findByUserIdAndPassword(String userId, String password);

    // 회원 대여 - 3권 이상 대여 제한
    List<User> findByUserRentCountLessThanEqual(int maxRentCount);

    // 회원 탈퇴 - 로그인된 회원의 아이디를 탈퇴 시킨다.
    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.userId = :userId")
    void deleteByUserId(String userId);

    // 관리자가 회원을 삭제하는 메서드
    void deleteById(Long id);

    List<User> findByUserNameContainingIgnoreCase(String userName);

    Page<User> findByUserNameContainingIgnoreCase(String userName, Pageable pageable);
}
