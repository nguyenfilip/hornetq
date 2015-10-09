package net.nguyen.journal.inspect;



import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Event - Base class for Candlepin events. Serves as both our semi-permanent
 * audit history in the database, as well as an integral part of the event
 * queue.
 */

@XmlRootElement(namespace = "http://fedorahosted.org/candlepin/Event")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class CandlepinEvent {


    /**
     * Type - Constant representing the type of this event.
     */
    public enum Type {
        CREATED, MODIFIED, DELETED
    }
    
    private String principal;
    
    

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    /**
     * Target the type of entity operated on.
     */
    public enum Target {
        CONSUMER, OWNER, ENTITLEMENT, POOL, EXPORT, IMPORT, USER, ROLE, SUBSCRIPTION, ACTIVATIONKEY, GUESTID, RULES, COMPLIANCE
    }

    /**
     * Describes the value in the referenceId field
     */
    public enum ReferenceType {
        POOL
    }

    private String id;

    private Type type;

    private Target target;

    private String targetName;

    private String principalStore;

    private Date timestamp;

    private String entityId;

    private String ownerId;

    private String consumerId;

    private String referenceId;

    private ReferenceType referenceType;

    private String oldEntity;
    private String newEntity;

    private String messageText;

    public void setId(String id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public ReferenceType getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(ReferenceType referenceType) {
        this.referenceType = referenceType;
    }

    @XmlTransient
    public String getPrincipalStore() {
        return principalStore;
    }

    public void setPrincipalStore(String principalStore) {
        this.principalStore = principalStore;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    @XmlTransient
    public String getOldEntity() {
        return oldEntity;
    }

    public void setOldEntity(String oldEntity) {
        this.oldEntity = oldEntity;
    }

    @XmlTransient
    public String getNewEntity() {
        return newEntity;
    }

    public void setNewEntity(String newEntity) {
        this.newEntity = newEntity;
    }

    @Override
    public String toString() {
        return "Event [" + "id=" + getId() + ", target=" + getTarget()
                + ", type=" + getType() + ", time=" + getTimestamp()
                + ", entity=" + getEntityId() + "]";
    }

    private String getId() {
        return id;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    /**
     * @return the targetName
     */
    public String getTargetName() {
        return targetName;
    }

    /**
     * @param targetName
     *            the targetName to set
     */
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    /**
     * @return the messageText
     */
    public String getMessageText() {
        return messageText;
    }

    /**
     * @param messageText
     *            the messageText to set
     */
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
