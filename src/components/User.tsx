import React, { use, useEffect, useRef, useState } from 'react';
import "../assets/css/user.css"
import { useNavigate } from 'react-router-dom';

const User: React.FC = () => {
    const [users,setUsers] = useState<any>([]);
    const [loading,setLoading] = useState(true);
    const [editingUser, setEditingUser] = useState<any | null>(null);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const hasRun = useRef(false);
    const navigate = useNavigate();
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');
    const api = "http://localhost:8080";

    useEffect(() => {
      if (hasRun.current) return;
      hasRun.current = true;

      if(role === "ADMIN") {
        fetchUser();  
      } else {
        alert('vui lòng đăng nhập với tài khoản admin')
        navigate("/login")
      }
        fetchUser();
    }, [token]);
    
    const fetchUser = async () => {
        try {
            const res = await fetch(`${api}/api/users`, {
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`,
            },
            });
            const data = await res.json();
            console.log(data)
            if(data) {
                setUsers(data)
            }
        } catch (err) {
            console.log("err: ", err);
        } finally {
            setLoading(false);
        }
    }

  const handleDeleteUser = async (id: number) => {
    try {
      const res = await fetch(`${api}/api/users/${id}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`,
        },
      });
      const data = await res.json();
      console.log(data);
      setUsers((prev: any[]) => prev.filter((user) => user.id !== id));
    } catch (err) {
      console.error("Error deleting user:", err);
    }
  };

  const handleUpdateUser = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!editingUser) return;

    try {
      const res = await fetch(`${api}/api/users/${editingUser.id}`, {
        method: "PUT",
        headers: { 
          "Content-Type": "application/json" ,
          "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(editingUser),
      });

      if (!res.ok) throw new Error("Cập nhật thất bại");

      const updatedUser = await res.json();

      // Cập nhật lại danh sách users
      setUsers((prev: any[]) =>
        prev.map((user) => (user.id === updatedUser.id ? updatedUser : user))
      );

      alert("Cập nhật thành công!");
      setIsModalOpen(false);
      setEditingUser(null);
    } catch (err) {
      console.error("Update user error:", err);
      alert("Có lỗi xảy ra khi cập nhật!");
    }
  };

  const handleEdit = (e: any) => {
      setEditingUser(e);
      setIsModalOpen(true);
  }

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setEditingUser((prev: any) => ({ ...prev, [name]: value }));
  };


    const handleLogout = () => {
      localStorage.removeItem('token')
      localStorage.removeItem('role')
      navigate("/login")
    }

    return (
        <>
        {loading ? (
            <>"đang tải"</>
        ) : (
            <div className="user-management">
      <div className="user-header">
        <h1>Quản lý Người dùng</h1>
        <div className="user-stats">
          <span>Tổng số: {users.length} nhân viên</span>
        </div>
        <button 
          className="logout-btn"
          onClick={handleLogout}>
            đăng xuất
        </button>
      </div>

      <div className="user-table-container">
        <table className="user-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>firstname</th>
              <th>lastname</th>
              <th>Email</th>
              <th>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user: any) => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.firstName}</td>
                <td>{user.lastName}</td>
                <td>{user.email}</td>
                
                <td className="action-buttons">
                  <button 
                    className="btn-edit"
                    onClick={() => handleEdit(user)}
                  >
                    Sửa
                  </button>
                  <button 
                    className="btn-delete"
                    onClick={() => handleDeleteUser(user.id)}
                  >
                    Xóa
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        {users.length === 0 && (
          <div className="no-data">
            <p>Không có dữ liệu người dùng</p>
          </div>
        )}
      </div>

      {/* Modal chỉnh sửa */}
      {isModalOpen && editingUser && (
        <div className="modal-overlay">
          <div className="modal-content">
            <div className="modal-header">
              <h2>Chỉnh sửa thông tin người dùng</h2>
              <button 
                className="close-btn"
                onClick={() => {
                  setIsModalOpen(false);
                  setEditingUser(null);
                }}
              >
                &times;
              </button>
            </div>
            <form onSubmit={handleUpdateUser} className="user-form">
              <div className="form-group">
                <label htmlFor="firstName">Họ:</label>
                <input
                  type="text"
                  id="firstName"
                  name="firstName"
                  value={editingUser.firstName || ""}
                  onChange={handleInputChange}
                  required
                />
              </div>              

              <div className="form-group">
                <label htmlFor="lastName">Tên:</label>
                <input
                  type="text"
                  id="lastName"
                  name="lastName"
                  value={editingUser.lastName || ""}
                  onChange={handleInputChange}
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="email">Email:</label>
                <input
                  type="email"
                  id="email"
                  name="email"
                  value={editingUser.email}
                  onChange={handleInputChange}
                  required
                />
              </div>
            
              <div className="form-actions">
                <button type="submit" className="btn-save">Lưu thay đổi</button>
                <button 
                  type="button" 
                  className="btn-cancel"
                  onClick={() => {
                    setIsModalOpen(false);
                    setEditingUser(null);
                  }}
                >
                  Hủy
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
        )}
        </>
    );
};

export default User;