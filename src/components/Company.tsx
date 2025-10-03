import React, { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import "../assets/css/company.css"

const Company: React.FC = () => {
  const [companies, setCompanies] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [editingCompany, setEditingCompany] = useState<any | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const navigate = useNavigate();
  const token = localStorage.getItem('token');
  const hasRun = useRef(false);
  const api = "http://localhost:8080";

  useEffect(() => {
    if (hasRun.current) {
    hasRun.current = true; 
  }

    if(!token) {
      alert('vui lòng đăng nhập')
      navigate("/login");
  }
    fetchCompany();
  }, [token]);

  const fetchCompany = async () => {
    try {
      const res = await fetch(`${api}/api/company`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      const data = await res.json();
      if (data) {
        setCompanies(data);
      }
    } catch (err) {
      console.log("err: ", err);
    } finally {
      setLoading(false);
    }
  };

  
  const handleDelete = async (companyId: number) => {
    if (window.confirm('Bạn có chắc chắn muốn xóa công ty này?')) {
      try {
        const res = await fetch(`${api}/api/company/${companyId}`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        });

        if (res.ok) {
          setCompanies(pred => pred.filter((c: any) => c.id !== companyId));
          alert('Xóa công ty thành công!');
        } else {
          alert('Xóa công ty thất bại!');
        }
      } catch (err) {
        console.log("err: ", err);
        alert('Có lỗi xảy ra khi xóa công ty!');
      }
    }
  };

  const handleSave = async (e: any) => {
    e.preventDefault();
    if (!editingCompany) return;

    try {
      const res = await fetch(`${api}/api/company/${editingCompany.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify(editingCompany),
      });

      if (res.ok) {
        setCompanies(companies.map((c: any) =>
          c.id === editingCompany.id ? editingCompany : c
        ));
        setIsModalOpen(false);
        setEditingCompany(null);
        alert('Cập nhật thông tin công ty thành công!');
      } else {
        alert('Cập nhật thông tin công ty thất bại!');
      }
    } catch (err) {
      console.log("err: ", err);
      alert('Có lỗi xảy ra khi cập nhật công ty!');
    }
  };

  const handleInputChange = (e: any) => {
    if (editingCompany) {
      setEditingCompany({
        ...editingCompany,
        [e.target.name]: e.target.value
      });
    }
  };

  const handleEdit = (company: any) => {
    setEditingCompany(company);
    setIsModalOpen(true);
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    navigate("/login");
  };

return (
  <>
    {loading ? (
      <div className="loading-container">
        <div className="loading-spinner"></div>
        <span>Đang tải dữ liệu...</span>
      </div>
    ) : (
      <div className="user-management">
        <div className="user-header">
          <h1>Quản lý Công ty</h1>
          <div className="user-stats">
            <span>Tổng số: {companies.length} công ty</span>
          </div>
          <button className="logout-btn" onClick={handleLogout}>
            Đăng xuất
          </button>
        </div>

        <div className="user-table-container">
          <table className="user-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Tên công ty</th>
                <th>Số người dùng</th>
                <th>Thao tác</th>
              </tr>
            </thead>
            <tbody>
              {companies.map((company: any) => (
                <tr key={company.id}>
                  <td>{company.id}</td>
                  <td>{company.companyName}</td>
                  <td>{company.users?.length || 0}</td>
                  <td className="action-buttons">
                    <button className="btn-edit" onClick={() => handleEdit(company)}>
                      Sửa
                    </button>
                    <button className="btn-delete" onClick={() => handleDelete(company.id)}>
                      Xóa
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {companies.length === 0 && (
            <div className="no-data">
              <p>Không có dữ liệu công ty</p>
            </div>
          )}
        </div>

        {/* Modal chỉnh sửa */}
        {isModalOpen && editingCompany && (
          <div className="modal-overlay">
            <div className="modal-content">
              <div className="modal-header">
                <h2>Chỉnh sửa thông tin công ty</h2>
                <button
                  className="close-btn"
                  onClick={() => {
                    setIsModalOpen(false);
                    setEditingCompany(null);
                  }}
                >
                  &times;
                </button>
              </div>
              <form onSubmit={handleSave} className="user-form">
                <div className="form-group">
                  <label htmlFor="companyName">Tên công ty:</label>
                  <input
                    type="text"
                    id="companyName"
                    name="companyName"
                    value={editingCompany.companyName}
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
                      setEditingCompany(null);
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

export default Company;
