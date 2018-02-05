package azkaban.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import azkaban.project.ProjectLoader;
import azkaban.project.ProjectManagerException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import azkaban.executor.ExecutorLoader;
import azkaban.executor.ExecutorManagerException;
import azkaban.executor.JdbcExecutorLoader;
import azkaban.utils.Props;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DBUserManager implements UserManager {
    private Props props;
    private ProjectLoader projectLoader;

    private HashMap<String, User> users = new HashMap<String, User>();
    private Map<String, Role> roles = new HashMap<>();
    private Map<String, Group> groups = new HashMap<String, Group>();
    //private HashMap<String, Set<String>> proxyUserMap = new HashMap<>();

    public DBUserManager(Props props) {
        this.props = props;
    }

    @Override
    public User getUser(String username, String password) throws UserManagerException {
        if (StringUtils.isBlank(username))
            throw new UserManagerException("username is empty!");

        if (StringUtils.isBlank(password))
            throw new UserManagerException("password is empty!");

        synchronized (this) {
            User user;
            try {
                user = projectLoader.selectUser(username);
            } catch (ProjectManagerException ex) {
                throw new UserManagerException(ex.getMessage());
            }
            if (user == null)
                throw new UserManagerException("this username is not exist!");

            if (!user.getPassword().equals(password))
                throw new UserManagerException("password error!");

            user.setPermissions(new User.UserPermissions() {
                @Override
                public boolean hasPermission(final String permission) {
                    return true;
                }

                @Override
                public void addPermission(final String permission) {
                }
            });

            users.put(username, user);
        }

        return users.get(username);
    }

    @Override
    public boolean validateUser(final String username) {
        return users.containsKey(username);
    }

    @Override
    public Role getRole(final String roleName) {
        return roles.get(roleName);
    }

    @Override
    public boolean validateGroup(final String groupName) {
        return groups.containsKey(groupName);
    }

    @Override
    public boolean validateProxyUser(final String proxyUser, User realUser) {
        // 没有对用户使用代理功能
        // 代理功能仅对作业设置
        return true;
        //if (this.proxyUserMap.containsKey(realUser.getUserId())
        //        && this.proxyUserMap.get(realUser.getUserId()).contains(proxyUser)) {
        //    return true;
        //} else {
        //    return false;
        //}
    }

    @Override
    public void setProjectLoader(ProjectLoader projectLoader) {
        this.projectLoader = projectLoader;

        // 服务启动之后，角色信息和组信息不会实时更新加载
        // 如果有新增的角色信息或组信息，需要重启 web 服务
        roles = projectLoader.selectRoles();
        groups = projectLoader.selectGroups();
    }
}

