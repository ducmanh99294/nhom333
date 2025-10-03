import React, { use, useEffect, useState } from 'react';
import "../assets/css/user.css"

const User: React.FC = () => {
    const [users,setUsers] = useState<any>([]);
    const [loading,setLoading] = useState(true);
    const [editingUser, setEditingUser] = useState<any | null>(null);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');
    const api = "http://localhost:8080";

    useEffect(() => {
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

    const handleEdit = (e: any) => {
        setEditingUser(e);
        setIsModalOpen(true);
    }

    const handleDelete = async (userId: any) => {
        if (window.confirm('Bạn có chắc chắn muốn xóa người dùng này?')) {
        try {
            const res = await fetch(`${api}/api/user/${userId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
            });
            
            if (res.ok) {
            setUsers(users.filter((user: any) => user.id !== userId));
            alert('Xóa người dùng thành công!');
            } else {
            alert('Xóa người dùng thất bại!');
            }
        } catch (err) {
            console.log("err: ", err);
            alert('Có lỗi xảy ra khi xóa người dùng!');
        }
        }
    };

    const handleSave = async (e: any) => {
        e.preventDefault();
        if (!editingUser) return;

        try {
        const res = await fetch(`${api}/api/user/${editingUser.id}`, {
            method: 'PUT',
            headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
            body: JSON.stringify(editingUser),
        });

        if (res.ok) {
            setUsers(users.map((user: any) => 
            user.id === editingUser.id ? editingUser : user
            ));
            setIsModalOpen(false);
            setEditingUser(null);
            alert('Cập nhật thông tin thành công!');
        } else {
            alert('Cập nhật thông tin thất bại!');
        }
        } catch (err) {
        console.log("err: ", err);
        alert('Có lỗi xảy ra khi cập nhật thông tin!');
        }
    };

    const handleInputChange = (e: any) => {
        if (editingUser) {
        setEditingUser({
            ...editingUser,
            [e.target.name]: e.target.value
        });
        }
    };

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
                    onClick={() => handleDelete(user.id)}
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
            <form onSubmit={handleSave} className="user-form">
              <div className="form-group">
                <label htmlFor="name">Họ tên:</label>
                <input
                  type="text"
                  id="name"
                  name="name"
                  value={editingUser.name}
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
              
              <div className="form-group">
                <label htmlFor="phone">Số điện thoại:</label>
                <input
                  type="tel"
                  id="phone"
                  name="phone"
                  value={editingUser.phone}
                  onChange={handleInputChange}
                  required
                />
              </div>
              
              <div className="form-group">
                <label htmlFor="position">Chức vụ:</label>
                <input
                  type="text"
                  id="position"
                  name="position"
                  value={editingUser.position}
                  onChange={handleInputChange}
                  required
                />
              </div>
              
              <div className="form-group">
                <label htmlFor="department">Phòng ban:</label>
                <input
                  type="text"
                  id="department"
                  name="department"
                  value={editingUser.department}
                  onChange={handleInputChange}
                  required
                />
              </div>
              
              <div className="form-group">
                <label htmlFor="status">Trạng thái:</label>
                <select
                  id="status"
                  name="status"
                  value={editingUser.status}
                  onChange={handleInputChange}
                >
                  <option value="active">Đang hoạt động</option>
                  <option value="inactive">Ngừng hoạt động</option>
                </select>
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